package com.muver.chars.server.util;

public class TooSmallContainerException extends RuntimeException {
    public TooSmallContainerException() {
        super("Not possible to insert the secret in a container that is too small.");
    }
}
