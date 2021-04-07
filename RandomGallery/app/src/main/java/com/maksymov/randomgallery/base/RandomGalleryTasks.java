package com.maksymov.randomgallery.base;

import java.util.concurrent.Callable;

import ua.cn.stu.randomgallery.RandomGalleryClient;

public class RandomGalleryTasks {

    private RandomGalleryClient client;

    public RandomGalleryTasks(RandomGalleryClient client) {
        this.client = client;
    }

    public Callable<Boolean> createCheckUpdatesTask() {
        return new CheckUpdatesTask(client);
    }

    public Callable<Boolean> createSyncTask() {
        return new SyncTask(client);
    }

}
