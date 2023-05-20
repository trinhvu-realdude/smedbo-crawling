package com.travis.smedbo.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    private Integer id;
    private String title;
    private String url;
    private Image image;
}
