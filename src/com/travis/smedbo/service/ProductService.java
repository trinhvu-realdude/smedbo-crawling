package com.travis.smedbo.service;

import com.travis.smedbo.constant.Constants;
import com.travis.smedbo.model.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProductService {


    /**
     * function getProductsByCategory()
     *
     * @param category
     * @return a list of product
     */
    public List<Product> getProductsByCategory(Category category) {
        List<Product> result = new ArrayList<>();
        String categoryUrl = category.getUrl();

        try {
            Document html = Jsoup.connect(Constants.BASE_URL + categoryUrl).get();

            int numberOfProducts = Integer.parseInt(html.getElementsByClass("woocommerce-result-count").text().replace("products", "").trim());

            // check total of products if it is more than 12, will check pagination to get more products
            if (numberOfProducts > 12) {
                if (numberOfProducts % 12 == 0) {
                    int pagination = numberOfProducts / 12;

                    System.out.println(pagination);
                } else {
                    int pagination = (int) Math.ceil((double) numberOfProducts / 12);

                    System.out.println(pagination);
                }
            } else {

            }
        } catch (IOException e) {
            System.err.println("Error: " + e);
        }

        return result;
    }


    /**
     * function detDetailsProduct()
     *
     * @param url
     * @return details of product
     */
    public Product getDetailsProduct(String url) {
        Product result = null;

        try {
            Document html = Jsoup.connect(Constants.BASE_URL + url).get();

            String title = html.getElementsByClass("product_title").text().trim();
            String price = html.getElementsByClass("price").text().trim();
            String description = html.getElementsByClass("woocommerce-product-details__short-description").size() > 0
                    ? html.getElementsByClass("woocommerce-product-details__short-description").get(0).text().replace("Description", "").trim()
                    : null;

            List<Image> images = new ArrayList<>();
            Elements imageList = html.getElementsByClass("woocommerce-product-gallery__image");

            for (Element elementImage : imageList) {
                String imageUrl = elementImage.getElementsByTag("a").attr("href");
                if (!imageUrl.equals("")) {
                    images.add(new Image(imageUrl));
                }
            }

            Element infoContainer = html.getElementsByClass("product_meta__list").get(0);
            String itemNumber = "";
            List<BathroomSeries> bathroomSeries = new ArrayList<>();
            List<String> surfaceColors = new ArrayList<>();

            for (int i = 0; i < infoContainer.childrenSize(); i++) {
                // item number
                if (i == 0) {
                    itemNumber = infoContainer.child(i).getElementsByClass("product_meta__value").text().trim();
                }

                // bathroom series
                if (i == 1) {
                    BathroomSeriesService bathroomSeriesService = new BathroomSeriesService();
                    List<String> bathroomSeriesTitleList = List.of(infoContainer.child(i).getElementsByClass("product_meta__value").text().trim().split(", "));
                    // write code to get Bathroom Series object
                    for (String bathroomSeriesTitle : bathroomSeriesTitleList) {
                        BathroomSeries bathroomSeriesObject = bathroomSeriesService.getBathroomSeriesByTitle(bathroomSeriesTitle);
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
            AdditionalInformation additionalInformation = new AdditionalInformation();
            if (additionalInformationContainer != null) {
                String otherInfo = additionalInformationContainer.getElementsByTag("p").text().trim();
                List<String> sketchSizes = new ArrayList<>();
                String imageUrl = html.getElementsByClass("single-product-additional-information-tab__sketch").get(0).child(0).attr("src");
                Image image = new Image(imageUrl);
                Element sketchSizesList = additionalInformationContainer.getElementsByClass("product-attributes__sub-list").get(0);

                for (int i = 0; i < sketchSizesList.childrenSize(); i++) {
                    String sketchSizesItem = sketchSizesList.child(i).getElementsByTag("span").text().trim();
                    sketchSizes.add(sketchSizesItem);
                }
                additionalInformation.setOtherInfo(otherInfo);
                additionalInformation.setSketchSizes(sketchSizes);
                additionalInformation.setImage(image);
            }

            result = Product.builder()
                    .itemNumber(itemNumber)
                    .title(title)
                    .price(price.equals("") ? null : price)
                    .url(url)
                    .description(description)
                    .surfaceColors(surfaceColors.size() > 0 ? surfaceColors : null)
                    .images(images)
                    .bathroomSeries(bathroomSeries)
                    .additionalInformation(additionalInformationContainer != null ? additionalInformation : null)
                    .build();
        } catch (IOException e) {
            System.err.println("Error: " + e);
        }

        return result;
    }
}
