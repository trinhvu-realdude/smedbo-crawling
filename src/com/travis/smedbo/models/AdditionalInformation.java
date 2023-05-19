package com.travis.smedbo.models;

import java.util.List;

public class AdditionalInformation {
    private String otherInfo;
    private List<String> sketchSizes;

    public String getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(String otherInfo) {
        this.otherInfo = otherInfo;
    }

    public List<String> getSketchSizes() {
        return sketchSizes;
    }

    public void setSketchSizes(List<String> sketchSizes) {
        this.sketchSizes = sketchSizes;
    }
}
