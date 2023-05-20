package com.travis.smedbo.model;

import java.util.List;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdditionalInformation {
    private String otherInfo;
    private List<String> sketchSizes;
    private Image image;
}
