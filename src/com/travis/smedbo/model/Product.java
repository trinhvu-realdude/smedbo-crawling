package com.travis.smedbo.model;

import java.util.List;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    private String sku;
    private String title;
    private String price;
    private String url;
    private String description;
    private List<String> surfaceColors;
    private List<Image> images;
    private List<BathroomSeries> bathroomSeries;
    private AdditionalInformation additionalInformation;
    private String installation;
    private String highResolutionImage;
    private Category category;
}
