package com.maksymov.randomgallery.base;

import java.util.concurrent.Callable;

import ua.cn.stu.randomgallery.ProgressListener;
import ua.cn.stu.randomgallery.RandomGalleryClient;

public class SyncTask implements Callable<Boolean> {
    private RandomGalleryClient client;
    public SyncTask(RandomGalleryClient client) {
        this.client = client;
    }

    @Override
    public Boolean call() throws Exception {
        return client.syncGallery(EMPTY_LISTENER);
    }

    private static ProgressListener EMPTY_LISTENER = p -> {};
}
