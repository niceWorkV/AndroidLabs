package com.maksymov.randomgallery.base;

public interface TaskSubject<T> {
    void addListener(TaskListener<T> listener);

    void removeListener(TaskListener<T> listener);

    void cancel();
}

