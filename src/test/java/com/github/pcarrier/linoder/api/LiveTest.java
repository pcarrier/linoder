package com.github.pcarrier.linoder.api;

import org.junit.Assert;

public class LiveTest {
    private final API api;

    API getAPI() {
        return api;
    }

    LiveTest() {
        String apiKey = System.getProperty("linoder.apikey");
        Assert.assertTrue(apiKey != null);
        api = new APIBuilder().withKey(apiKey).build();
    }
}
