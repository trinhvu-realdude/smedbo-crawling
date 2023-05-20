package com.travis.smedbo.service.job;

import com.travis.smedbo.constant.Constants;
import com.travis.smedbo.model.BathroomSeries;
import com.travis.smedbo.model.Category;
import com.travis.smedbo.model.Product;
import com.travis.smedbo.service.BathroomSeriesService;
import com.travis.smedbo.service.CategoryService;
import com.travis.smedbo.service.JsonFileService;
import com.travis.smedbo.service.ProductService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CrawlingJob {

    public void run() {
        try {
            JsonFileService json = JsonFileService.getInstance();

            // step 1: get Category -> save JSON
            CategoryService categoryService = new CategoryService();
            List<Category> categoryList = categoryService.getAllCategories();
            String categoryData = json.generateDataToJson(categoryList);
            json.writeJsonFile(Constants.CATEGORIES, categoryData);

            // step 2: get Bathroom Series -> save JSON
            BathroomSeriesService bathroomSeriesService = new BathroomSeriesService();
            List<BathroomSeries> bathroomSeriesList = bathroomSeriesService.getAllBathroomSeries();
            String bathroomSeriesData = json.generateDataToJson(bathroomSeriesList);
            json.writeJsonFile(Constants.BATHROOM_SERIES, bathroomSeriesData);

            // step 3: get Products By Category -> get Products by Pagination -> get Product (get Bathroom Series) -> save JSON
            ProductService productService = new ProductService();
            List<Product> finalProductList = new ArrayList<>();
            for (Category category : categoryList) {
                List<Product> productList = productService.getProductsByCategory(category, bathroomSeriesList);
                finalProductList.addAll(productList);
            }
            String productData = json.generateDataToJson(finalProductList);
            json.writeJsonFile(Constants.PRODUCTS, productData);

            System.out.println("Done!!!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
