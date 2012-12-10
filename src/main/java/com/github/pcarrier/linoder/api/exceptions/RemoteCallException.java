package com.github.pcarrier.linoder.api.exceptions;

public abstract class RemoteCallException extends Exception {
    protected RemoteCallException() {
    }

    protected RemoteCallException(Throwable e) {
        super(e);
    }
}
