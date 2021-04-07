package com.maksymov.randomgallery.base;

public interface TaskListener<T> {
    void onResults(Result<T> results);
}
