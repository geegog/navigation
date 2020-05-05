package com.wemakesoftware.navigation.common.application.exception;

import java.util.UUID;

public class BaseNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;

    public BaseNotFoundException() {
        super();
    }

    public BaseNotFoundException(UUID id) {
        super(String.format("Base not found! : (Base id: %s)", id));
    }
}
