package com.github.pcarrier.linoder.api.exceptions;

import java.util.List;

public class APIFailureException extends RemoteCallException {
    private final List<String> errors;

    public APIFailureException(List<String> errors) {
        this.errors = errors;
    }

    @Override
    public String getMessage() {
        StringBuilder messageBuilder = new StringBuilder();

        for (String error : errors) {
            messageBuilder.append(error);
            messageBuilder.append(", ");
        }

        if (messageBuilder.length() > 2) {
            messageBuilder.setLength(messageBuilder.length() - 2);
        }

        return messageBuilder.toString();
    }
}
