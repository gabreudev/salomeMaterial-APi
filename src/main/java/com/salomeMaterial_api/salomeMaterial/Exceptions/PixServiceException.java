package com.salomeMaterial_api.salomeMaterial.Exceptions;

public class PixServiceException extends RuntimeException {
    public PixServiceException(String message) {
        super(message);
    }

    public PixServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
