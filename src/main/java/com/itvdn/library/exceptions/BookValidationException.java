package com.itvdn.library.exceptions;

public class BookValidationException extends Exception {
    public BookValidationException(String message) {
        super(message);
    }

    public BookValidationException(Throwable cause) {
        super(cause);
    }
}
