package com.travis.smedbo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.travis.smedbo.constant.Constants;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JsonFileService {

    private static final Logger LOGGER = Logger.getLogger(JsonFileService.class.getName());

    private static class JsonFileServiceHelper {
        public static final JsonFileService INSTANCE = new JsonFileService();
    }

    private JsonFileService() {

    }

    public static JsonFileService getInstance() {
        return JsonFileServiceHelper.INSTANCE;
    }

    public String generateDataToJson(Object data) throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(data);
    }

    public void writeJsonFile(String fileName, String data) throws IOException {
        LOGGER.log(Level.WARNING, "Writing " + fileName);
        FileWriter writer = new FileWriter(Constants.BASE_PATH + fileName);
        writer.write(data);
        writer.close();
    }

    public void sleep() throws InterruptedException {
        Random random = new Random();
        int randomNumber = random.nextInt(5001) + 5000;

        LOGGER.log(Level.INFO, "Sleeping in " + randomNumber + " milliseconds");
        Thread.sleep(randomNumber);
    }
}
