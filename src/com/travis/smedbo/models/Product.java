package com.travis.smedbo.models;

import java.util.List;

public class Product {
    private String itemNumber;
    private String title;
    private String url;
    private String description;
    private String surfaceColor;
    private List<Image> images;
    private List<BathroomSeries> bathroomSeries;
    private AdditionalInformation additionalInformation;

    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    public String getTitle() {
        return title;
    }

    public List<BathroomSeries> getBathroomSeries() {
        return bathroomSeries;
    }

    public void setBathroomSeries(List<BathroomSeries> bathroomSeries) {
        this.bathroomSeries = bathroomSeries;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public String getSurfaceColor() {
        return surfaceColor;
    }

    public void setSurfaceColor(String surfaceColor) {
        this.surfaceColor = surfaceColor;
    }

    public AdditionalInformation getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(AdditionalInformation additionalInformation) {
        this.additionalInformation = additionalInformation;
    }
}
