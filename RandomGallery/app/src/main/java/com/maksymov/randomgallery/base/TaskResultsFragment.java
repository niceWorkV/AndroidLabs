package com.maksymov.randomgallery.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TaskResultsFragment extends Fragment {

    public static final String TAG =
            TaskResultsFragment.class.getSimpleName();

    public TaskSubject<Boolean> hasUpdatesSubject;
    public TaskSubject<Boolean> syncSubject;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (hasUpdatesSubject != null) {
            hasUpdatesSubject.cancel();
        }
        if (syncSubject != null) {
            syncSubject.cancel();
        }
    }

}
