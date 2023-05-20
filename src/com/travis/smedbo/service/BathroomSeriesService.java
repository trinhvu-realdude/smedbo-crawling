package com.travis.smedbo.service;

import com.travis.smedbo.constant.Constants;
import com.travis.smedbo.model.BathroomSeries;
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

public class BathroomSeriesService {

    private static final Logger LOGGER = Logger.getLogger(BathroomSeriesService.class.getName());

    private static final JsonFileService json = JsonFileService.getInstance();

    /**
     * function getAllBathroomSeries()
     *
     * @return a list of bathroom series
     */
    public List<BathroomSeries> getAllBathroomSeries() {
        List<BathroomSeries> result = new ArrayList<>();

        try {
            Document html = Jsoup.connect(Constants.BASE_URL + "/bathroom-series/").get();
            LOGGER.log(Level.WARNING, "Fetching " + Constants.BASE_URL + "/bathroom-series/");

            Elements bathroomSeriesList = html.getElementsByClass("cta-3x33");

            int id = 1;

            for (Element section : bathroomSeriesList) {
                Element inside = section.child(0);

                for (int i = 0; i < inside.childrenSize(); i++) {
                    BathroomSeries bathroomSeries = new BathroomSeries();

                    String title = inside.child(i).getElementsByClass("cta-3x33__title").text();
                    String url = inside.child(i).attr("href").replace(Constants.BASE_URL, "").trim();
                    String description = inside.child(i).getElementsByTag("button").text().trim();
                    String imageUrl = inside.child(i).getElementsByTag("img").attr("src");
                    Image image = new Image(imageUrl);

                    bathroomSeries.setId(id++);
                    bathroomSeries.setTitle(title);
                    bathroomSeries.setUrl(url);
                    bathroomSeries.setDescription(description);
                    bathroomSeries.setImage(image);

                    result.add(bathroomSeries);
                }
            }

            LOGGER.log(Level.INFO, "Bathroom Series: " + json.generateDataToJson(result));

            json.sleep();
        } catch (IOException | InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Error: " + e);
        }

        return result;
    }
}
