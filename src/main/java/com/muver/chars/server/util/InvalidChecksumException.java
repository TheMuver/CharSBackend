package com.muver.chars.server.util;

public class InvalidChecksumException extends RuntimeException {
    public InvalidChecksumException() {
        super("The resulting external checksum does not match the secret checksum.");
    }
}
