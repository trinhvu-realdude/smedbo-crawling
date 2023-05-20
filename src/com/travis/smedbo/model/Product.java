package com.travis.smedbo.model;

import java.util.List;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    private String itemNumber;
    private String title;
    private String price;
    private String url;
    private String description;
    private List<String> surfaceColors;
    private List<Image> images;
    private List<BathroomSeries> bathroomSeries;
    private AdditionalInformation additionalInformation;
    private Category category;
}
