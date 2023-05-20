package com.travis.smedbo.service;

import com.travis.smedbo.constant.Constants;
import com.travis.smedbo.model.Category;
import com.travis.smedbo.model.Image;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CategoryService {

    private static final Logger LOGGER = Logger.getLogger(CategoryService.class.getName());

    private static final JsonFileService json = JsonFileService.getInstance();

    /**
     * function getAllCategories()
     *
     * @return a list of category
     */
    public List<Category> getAllCategories() {
        List<Category> result = new ArrayList<>();

        try {
            Document html = Jsoup.connect(Constants.BASE_URL + "/assortment/").get();
            LOGGER.log(Level.WARNING, "Fetching " + Constants.BASE_URL + "/assortment/");

            Elements categoryList = html.getElementsByClass("cta-4x25");

            int id = 1;

            for (Element section : categoryList) {
                Element inside = section.child(0);

                for (int i = 0; i < inside.childrenSize(); i++) {
                    Category category = new Category();

                    String title = inside.child(i).getElementsByClass("cta-4x25__title").text();
                    String url = inside.child(i).attr("href").replace(Constants.BASE_URL, "").trim();
                    String imageUrl = inside.child(i).getElementsByTag("img").attr("src");
                    Image image = new Image(imageUrl);

                    if (!title.equals("") && !url.equals("") && !imageUrl.equals("")) {
                        category.setId(id++);
                        category.setTitle(title);
                        category.setUrl(url);
                        category.setImage(image);

                        result.add(category);
                    }
                }
            }

            LOGGER.log(Level.INFO, "Categories: " + json.generateDataToJson(result));

            json.sleep();
        } catch (IOException | InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Error: " + e);
        }

        return result;
    }

}
