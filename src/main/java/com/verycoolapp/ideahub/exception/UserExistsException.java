package com.verycoolapp.ideahub.exception;

public class UserExistsException extends RuntimeException {

    public UserExistsException() {
        super("A user already exists with this phone number.");
    }

}
