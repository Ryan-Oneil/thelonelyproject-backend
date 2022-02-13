package org.lonelyproject.backend.exception;

public class ProfileAlreadyRegistered extends RuntimeException {

    public ProfileAlreadyRegistered(String message) {
        super(message);
    }
}
