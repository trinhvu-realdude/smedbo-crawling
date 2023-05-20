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

public class BathroomSeriesService {


    /**
     * function getAllBathroomSeries()
     *
     * @return a list of bathroom series
     */
    public List<BathroomSeries> getAllBathroomSeries() {
        List<BathroomSeries> result = new ArrayList<>();

        try {
            Document html = Jsoup.connect(Constants.BASE_URL + "/bathroom-series/").get();

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
        } catch (IOException e) {
            System.err.println("Error: " + e);
        }

        return result;
    }


    /**
     * function getBathroomSeriesByTitle()
     *
     * @param title
     * @return a bathroom series
     */
    public BathroomSeries getBathroomSeriesByTitle(String title) {
        List<BathroomSeries> bathroomSeriesList = getAllBathroomSeries();

        for (BathroomSeries bathroomSeries : bathroomSeriesList) {
            if (bathroomSeries.getTitle().equals(title)) return bathroomSeries;
        }

        return null;
    }
}
