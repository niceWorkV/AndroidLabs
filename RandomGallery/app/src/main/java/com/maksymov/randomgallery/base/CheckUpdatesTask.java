package com.maksymov.randomgallery.base;

import java.util.concurrent.Callable;

import ua.cn.stu.randomgallery.RandomGalleryClient;

public class CheckUpdatesTask implements Callable<Boolean> {
    private RandomGalleryClient client;

    public CheckUpdatesTask(RandomGalleryClient client) {
        this.client = client;
    }

    @Override
    public Boolean call() throws Exception {
        return client.hasUpdates();
    }
}
