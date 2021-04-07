package com.maksymov.randomgallery.base;

public class CancelledException extends RuntimeException {
    public CancelledException() {
        super("Task has been cancelled");
    }
}
