package com.github.pcarrier.linoder.api.exceptions;

public final class NetworkFailureException extends RemoteCallException {
    public NetworkFailureException(Exception e) {
        super(e);
    }
}
