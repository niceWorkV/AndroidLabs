package com.maksymov.randomgallery;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;

import com.maksymov.randomgallery.screens.sync.SyncState;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import ua.cn.stu.randomgallery.GalleryStorage;
import ua.cn.stu.randomgallery.RandomGalleryClient;

public class App extends Application {

    private static final String GALLERY_ID = "GALLERY_ID";

    private RandomGalleryClient galleryClient;
    private Picasso picasso;
    private SyncState syncState;

    @Override
    public void onCreate() {
        super.onCreate();

        // called on App start

        // creating sync state
        syncState = new SyncState(this);

        // getting Gallery ID. If it does not exist,
        // create a new random  Gallery ID
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String galleryId = preferences.getString(GALLERY_ID, "");
        if (galleryId.isEmpty()) {
            galleryId = generateNewGalleryId();
            preferences.edit()
                    .putString(GALLERY_ID, galleryId)
                    .apply();
        }

        // initializing Gallery client with the custom storage
        GalleryStorage galleryStorage = createStorage();
        galleryClient = new RandomGalleryClient(galleryId, galleryStorage);

        // initializing request handler for Picasso library.
        // it will load images into ImageViews
        GalleryRequestHandler requestHandler = new GalleryRequestHandler();
        galleryClient.addListener(requestHandler);

        // instantiating Picasso for loading images
        picasso = new Picasso.Builder(this)
                .addRequestHandler(requestHandler)
                .build();
    }

    public SyncState getSyncState() {
        // holds the background service state
        return syncState;
    }

    public RandomGalleryClient getGalleryClient() {
        // client which provides methods for working with gallery
        return galleryClient;
    }

    public Picasso getPicasso() {
        // Picasso library is used to prepare and display images
        return picasso;
    }

    // ---

    private GalleryStorage createStorage() {
        return new SimpleGallaryStorage(this);
    }

    private String generateNewGalleryId() {
        return UUID.randomUUID().toString();
    }
}
