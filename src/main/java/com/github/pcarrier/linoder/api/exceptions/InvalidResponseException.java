package com.github.pcarrier.linoder.api.exceptions;

public final class InvalidResponseException extends RemoteCallException {
    public InvalidResponseException(Exception e) {
        super(e);
    }
}
