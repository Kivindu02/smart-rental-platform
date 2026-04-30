package com.sp.reviewservice.exception;

public class AlreadyReviewedException extends RuntimeException {
    public AlreadyReviewedException(String message) {
        super(message);
    }
}
