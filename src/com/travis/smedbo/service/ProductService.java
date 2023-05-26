package com.travis.smedbo.service;

import com.travis.smedbo.constant.Constants;
import com.travis.smedbo.model.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ProductService {

    private static final Logger LOGGER = Logger.getLogger(ProductService.class.getName());

    private static final JsonFileService json = JsonFileService.getInstance();

    /**
     * function getProductsByCategory()
     *
     * @param category
     * @param bathroomSeriesList
     * @return a list of product
     */
    public List<Product> getProductsByCategory(Category category, List<BathroomSeries> bathroomSeriesList) {
        List<Product> result = new ArrayList<>();
        String categoryUrl = category.getUrl();

        try {
            Document html = Jsoup.connect(Constants.BASE_URL + categoryUrl).get();
            LOGGER.log(Level.WARNING, "Fetching " + Constants.BASE_URL + categoryUrl);

            int numberOfProducts = Integer.parseInt(html.getElementsByClass("woocommerce-result-count").text().replace("products", "").replace("product", "").trim());

            // get data from first page
            Element productList = html.getElementsByClass("shop-products__list").get(0);

            addProductToList(productList, result, category, bathroomSeriesList);

            // check total of products if it is more than 12, will check pagination to get more products
            if (numberOfProducts > 12) {
                int finalPage = numberOfProducts % 12 == 0 ? numberOfProducts / 12 : (int) Math.ceil((double) numberOfProducts / 12);
                getProductListByPage(result, finalPage, categoryUrl, category, bathroomSeriesList);
            }

            json.sleep();
        } catch (IOException | InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Error: " + e);
        }

        return result;
    }

    public void getProductListByPage(List<Product> result, int finalPage, String categoryUrl, Category category, List<BathroomSeries> bathroomSeriesList) {
        try {
            for (int pagination = 2; pagination <= finalPage; pagination++) {
                Document html = Jsoup.connect(Constants.BASE_URL + categoryUrl + "page/" + pagination + "/").get();
                LOGGER.log(Level.WARNING, "Fetching " + Constants.BASE_URL + categoryUrl + "page/" + pagination + "/");

                Element productList = html.getElementsByClass("shop-products__list").get(0);

                addProductToList(productList, result, category, bathroomSeriesList);

                json.sleep();
            }
        } catch (IOException | InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Error: " + e);
        }
    }

    public void addProductToList(Element productList, List<Product> result, Category category, List<BathroomSeries> bathroomSeriesList) {
        for (int i = 0; i < productList.childrenSize(); i++) {
            String productUrl = productList.child(i).getElementsByTag("a").attr("href").replace(Constants.BASE_URL, "");

            // get details of product
            Product product = getDetailsProduct(productUrl, category, bathroomSeriesList);
            result.add(product);
        }
    }


    /**
     * function getDetailsProduct()
     *
     * @param url
     * @param bathroomSeriesList
     * @return details of product
     */
    public Product getDetailsProduct(String url, Category category, List<BathroomSeries> bathroomSeriesList) {
        Product result = null;

        try {
            Document html = Jsoup.connect(Constants.BASE_URL + url).get();
            LOGGER.log(Level.WARNING, "Fetching: " + Constants.BASE_URL + url);

            String title = html.getElementsByClass("product_title").text().trim();
            String price = html.getElementsByClass("price").text().trim();
            String description = html.getElementsByClass("woocommerce-product-details__short-description")
                    .stream()
                    .findFirst()
                    .map(element -> element.text().replace("Description", "").trim())
                    .orElse(null);

            List<Image> images = html.getElementsByClass("woocommerce-product-gallery__image")
                    .stream()
                    .map(element -> new Image(element.getElementsByTag("a").attr("href")))
                    .filter(imageUrl -> !imageUrl.getUrl().isEmpty())
                    .collect(Collectors.toList());

            Element infoContainer = html.getElementsByClass("product_meta__list").get(0);
            String sku = "";
            List<BathroomSeries> bathroomSeries = new ArrayList<>();
            List<String> surfaceColors = new ArrayList<>();

            for (int i = 0; i < infoContainer.childrenSize(); i++) {
                // item number
                if (i == 0) {
                    sku = infoContainer.child(i).getElementsByClass("product_meta__value").text().trim();
                }

                // bathroom series
                if (i == 1) {
                    List<String> bathroomSeriesTitleList = List.of(infoContainer.child(i).getElementsByClass("product_meta__value").text().trim().split(", "));
                    // write code to get Bathroom Series object
                    for (String bathroomSeriesTitle : bathroomSeriesTitleList) {
                        BathroomSeries bathroomSeriesObject = getBathroomSeriesByTitle(bathroomSeriesTitle, bathroomSeriesList);
                        bathroomSeries.add(
                                bathroomSeriesObject == null
                                        ? BathroomSeries.builder().title(bathroomSeriesTitle).build()
                                        : bathroomSeriesObject
                        );
                    }
                }

                // surface and color
                if (i == 2) {
                    surfaceColors = List.of(infoContainer.child(i).getElementsByClass("product_meta__value").text().trim().split(", "));
                }
            }

            Element additionalInformationContainer = html.getElementsByTag("table").size() > 0
                    ? html.getElementsByTag("table").get(0).child(0)
                    : null;
            Map<String, String> attributes = new HashMap<>();
            Map<String, String> sketchSizes = new HashMap<>();
            String imageUrl = "";
            if (additionalInformationContainer != null) {
                if (!additionalInformationContainer.child(0).className().split("--")[1].equals("sketch_sizes")) {
                    String keyAttributes = additionalInformationContainer.child(0).getElementsByTag("th").text().trim();
                    String valueAttributes = additionalInformationContainer.child(0).getElementsByTag("td").text().trim();
                    attributes.put(keyAttributes, valueAttributes);
                }

                Element sketchSizesList = additionalInformationContainer.getElementsByClass("product-attributes__sub-list")
                        .stream()
                        .findFirst()
                        .orElse(null);

                if (sketchSizesList != null) {
                    sketchSizesList.children().forEach(element -> {
                        String sketchSizesOption = element.getElementsByTag("strong").text().replace(":", "").trim();
                        String sketchSizesItem = element.getElementsByTag("span").text().trim();
                        sketchSizes.put(sketchSizesOption, sketchSizesItem);
                    });
                }

                imageUrl = html.getElementsByClass("single-product-additional-information-tab__sketch")
                        .stream()
                        .findFirst()
                        .flatMap(element -> element.children().stream().findFirst())
                        .map(element -> element.attr("src"))
                        .orElse(null);
            }

            AdditionalInformation additionalInformation = AdditionalInformation.builder()
                    .attributes(attributes.size() > 0 ? attributes : null)
                    .sketchSizes(sketchSizes.size() > 0 ? sketchSizes : null)
                    .thumbnail(imageUrl == null || imageUrl.equals("") ? null : new Image(imageUrl))
                    .build();

            String installation = html.getElementsByClass("installation_tab")
                    .stream()
                    .findFirst()
                    .map(element -> element.getElementsByTag("a").attr("href"))
                    .orElse(null);

            String highResolutionImage = html.getElementsByClass("images_tab")
                    .stream()
                    .findFirst()
                    .map(element -> element.getElementsByTag("a").attr("href"))
                    .orElse(null);

            result = Product.builder()
                    .sku(sku)
                    .title(title)
                    .price(price.equals("") ? null : price)
                    .url(url)
                    .description(description)
                    .surfaceColors(surfaceColors.size() > 0 ? surfaceColors : null)
                    .images(images)
                    .bathroomSeries(bathroomSeries)
                    .additionalInformation(additionalInformationContainer != null ? additionalInformation : null)
                    .installation(installation == null || installation.equals("") ? null : installation)
                    .highResolutionImage(highResolutionImage == null || highResolutionImage.equals("") ? null : highResolutionImage)
                    .category(category)
                    .build();

            LOGGER.log(Level.INFO, "Product: " + json.generateDataToJson(result));

            json.sleep();
        } catch (IOException | InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Error: " + e);
        }

        return result;
    }

    public BathroomSeries getBathroomSeriesByTitle(String title, List<BathroomSeries> bathroomSeriesList) {
        for (BathroomSeries bathroomSeries : bathroomSeriesList) {
            if (bathroomSeries.getTitle().equals(title)) return bathroomSeries;
        }

        return null;
    }
}
