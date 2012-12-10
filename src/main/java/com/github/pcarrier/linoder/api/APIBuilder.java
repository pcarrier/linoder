package com.github.pcarrier.linoder.api;

import us.monoid.web.Resty;

public class APIBuilder {
    private String apiKey = null;
    private String baseURI = "https://api.linode.com/";
    private boolean debug = false;

    @SuppressWarnings("UnusedDeclaration")
    public APIBuilder withBaseURI(String baseURI) {
        this.baseURI = baseURI;
        return this;
    }

    @SuppressWarnings("UnusedDeclaration")
    public APIBuilder withKey(String apiKey) {
        this.apiKey = apiKey;
        return this;
    }

    @SuppressWarnings("UnusedDeclaration")
    public APIBuilder withDebug(boolean debug) {
        this.debug = debug;
        return this;
    }

    public API build() {
        final Resty resty = new Resty();

        final StringBuilder baseURIBuilder = new StringBuilder(baseURI);
        baseURIBuilder.append("?");

        if (apiKey != null) {
            baseURIBuilder.append("api_key=");
            baseURIBuilder.append(apiKey);
            baseURIBuilder.append("&");
        }

        return new API(resty, baseURIBuilder.toString(), debug);
    }
}
