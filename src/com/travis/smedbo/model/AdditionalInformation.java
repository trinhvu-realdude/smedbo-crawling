package com.travis.smedbo.model;

import java.util.List;
import java.util.Map;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdditionalInformation {
    private Map<String, String> attributes;
    private Map<String, String> sketchSizes;
    private Image thumbnail;
}
