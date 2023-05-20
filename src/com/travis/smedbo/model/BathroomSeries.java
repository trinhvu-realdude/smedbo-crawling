package com.travis.smedbo.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BathroomSeries {
    private Integer id;
    private String title;
    private String url;
    private String description;
    private Image image;
}
