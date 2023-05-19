package com.travis.smedbo.app;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.travis.smedbo.models.BathroomSeries;
import com.travis.smedbo.services.BathroomSeriesService;
import com.travis.smedbo.services.JsonFileService;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        try {
//            CategoryService categoryService = new CategoryService();
//
//            List<Category> categoryList = categoryService.getAllCategories();

            BathroomSeriesService bathroomSeriesService = new BathroomSeriesService();
            List<BathroomSeries> bathroomSeriesList = bathroomSeriesService.getAllBathroomSeries();

            JsonFileService jsonFileService = new JsonFileService();
            System.out.println(jsonFileService.generateDataToJson(bathroomSeriesList));
        }
        catch (JsonProcessingException e) {
            System.err.println("Error: " + e);
        }
    }
}
