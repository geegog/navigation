package com.wemakesoftware.navigation.common.application.exception;

import java.util.UUID;

public class MobileNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;

    public MobileNotFoundException() {
        super();
    }

    public MobileNotFoundException(UUID id) {
        super(String.format("Mobile station not found! : (Base id: %s)", id));
    }
}
