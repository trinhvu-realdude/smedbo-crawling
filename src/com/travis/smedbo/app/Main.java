package com.travis.smedbo.app;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.travis.smedbo.model.Category;
import com.travis.smedbo.model.Image;
import com.travis.smedbo.model.Product;
import com.travis.smedbo.service.JsonFileService;
import com.travis.smedbo.service.ProductService;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        try {
//            CategoryService categoryService = new CategoryService();
//            List<Category> categoryList = categoryService.getAllCategories();

            Category category = new Category();
//            category.setId(15);
//            category.setTitle("Accessories");
//            category.setUrl("/assortment/accessories/");
//            category.setImage(new Image("https://www.smedbo.com/wp-content/uploads/sites/8/2021/11/tillbehor-YK358-B2000px-1024x243.jpg"));

//            category.setId(2);
//            category.setTitle("Bathroom shelfs");
//            category.setUrl("/assortment/bathroom-shelves/");
//            category.setImage(new Image("https://www.smedbo.com/wp-content/uploads/sites/8/2021/12/badrumshylla-HK347-B2000px-1024x845.jpg"));

            ProductService productService = new ProductService();
//            List<Product> productList = productService.getProductsByCategory(category);
//            Product product = productService.getDetailsProduct("/product/cleaning-cloth/"); // no surface and color and no additional information
            Product product = productService.getDetailsProduct("/product/bathroom-clock-2/"); // this one has a list of bathroomSeries, no other info
//            Product product = productService.getDetailsProduct("/product/fv952/"); // no additional information, no description
//            Product product = productService.getDetailsProduct("/product/grout-line-corner-shelf-6/"); // full


//            BathroomSeriesService bathroomSeriesService = new BathroomSeriesService();
//            List<BathroomSeries> bathroomSeriesList = bathroomSeriesService.getAllBathroomSeries();

            JsonFileService jsonFileService = new JsonFileService();
//            System.out.println(jsonFileService.generateDataToJson(bathroomSeriesList));
//            System.out.println(jsonFileService.generateDataToJson(categoryList));
//            System.out.println(jsonFileService.generateDataToJson(productList));
            System.out.println(jsonFileService.generateDataToJson(product));
        } catch (JsonProcessingException e) {
            System.err.println("Error: " + e);
        }
    }
}
