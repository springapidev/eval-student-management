package com.rajaul.studentmanagement.exception;
/**
 * @author Mohammad Rajaul Islam
 * @version 1.0.0
 * @since 1.0.0
 */
public class UniqueConstraintViolationException extends RuntimeException {
    public UniqueConstraintViolationException(String message) {
        super(message);
    }
}
