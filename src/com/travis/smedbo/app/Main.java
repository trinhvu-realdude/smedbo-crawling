package com.travis.smedbo.app;

import com.travis.smedbo.service.job.CrawlingJob;

public class Main {

    public static void main(String[] args) {
        CrawlingJob crawling = new CrawlingJob();
        crawling.run();
    }
}
