package com.travis.smedbo.app;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.travis.smedbo.models.Category;
import com.travis.smedbo.services.CategoryService;
import com.travis.smedbo.services.JsonFileService;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        try {
            CategoryService categoryService = new CategoryService();

            List<Category> categoryList = categoryService.getAllCategories();

            JsonFileService jsonFileService = new JsonFileService();
            System.out.println(jsonFileService.generateDataToJson(categoryList));
        }
        catch (JsonProcessingException e) {
            System.err.println("Error: " + e);
        }
    }
}
