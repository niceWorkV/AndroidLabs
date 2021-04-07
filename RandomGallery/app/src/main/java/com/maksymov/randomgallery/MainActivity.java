package com.maksymov.randomgallery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.Fade;
import android.transition.TransitionSet;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.maksymov.randomgallery.R;
import com.maksymov.randomgallery.base.RandomGalleryTasks;
import com.maksymov.randomgallery.base.Status;
import com.maksymov.randomgallery.base.TaskListener;
import com.maksymov.randomgallery.base.TaskManagerFragment;
import com.maksymov.randomgallery.base.TaskResultsFragment;
import com.maksymov.randomgallery.screens.Router;
import com.maksymov.randomgallery.screens.details.DetailsFragment;
import com.maksymov.randomgallery.screens.gallery.GalleryFragment;
import com.maksymov.randomgallery.screens.sync.ActionsHandlerService;
import com.maksymov.randomgallery.screens.sync.SyncService;
import com.maksymov.randomgallery.screens.sync.SyncState;

public class MainActivity
        extends AppCompatActivity
        implements Router {

    private App app;

    private TextView messageTextView;
    private ProgressBar syncProgressBar;
    private TextView actionTextView;
    private View messageContainer;

    private TaskManagerFragment taskManagerFragment;
    private TaskResultsFragment taskResultsFragment;

    private RandomGalleryTasks tasks;

    private boolean syncInProgress;
    private boolean hasUpdates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        app = (App) getApplicationContext();

        tasks = new RandomGalleryTasks(app.getGalleryClient());

        if (savedInstanceState == null) {
            // clean launch, no fragments
            taskManagerFragment = new TaskManagerFragment();
            taskResultsFragment = new TaskResultsFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragmentContainer, new GalleryFragment())
                    .add(taskManagerFragment, TaskManagerFragment.TAG)
                    .add(taskResultsFragment, TaskResultsFragment.TAG)
                    .commit();
        } else {
            taskManagerFragment =
                    (TaskManagerFragment) getSupportFragmentManager()
                            .findFragmentByTag(TaskManagerFragment.TAG);
            taskResultsFragment =
                    (TaskResultsFragment) getSupportFragmentManager()
                            .findFragmentByTag(TaskResultsFragment.TAG);
        }

        actionTextView = findViewById(R.id.actionTextView);
        actionTextView.setOnClickListener(v -> {
            if (taskResultsFragment.syncSubject != null) {
                taskResultsFragment.syncSubject
                        .removeListener(syncListener);
            }

            taskResultsFragment.syncSubject = taskManagerFragment
                    .submitTask(tasks.createSyncTask());
            taskResultsFragment.syncSubject
                    .addListener(syncListener);
        });

        messageTextView = findViewById(R.id.messageTextView);
        messageContainer = findViewById(R.id.messageContainer);
        syncProgressBar = findViewById(R.id.syncProgressBar);

        updateUi();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (taskResultsFragment.hasUpdatesSubject == null) {
            taskResultsFragment.hasUpdatesSubject =
                    taskManagerFragment
                            .submitTask(tasks.createCheckUpdatesTask());
        }

        taskResultsFragment.hasUpdatesSubject
                .addListener(hasUpdatesListener);

        if (taskResultsFragment.syncSubject != null) {
            taskResultsFragment.syncSubject
                    .addListener(syncListener);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        taskResultsFragment.hasUpdatesSubject
                .removeListener(hasUpdatesListener);
        if (taskResultsFragment.syncSubject != null) {
            taskResultsFragment.syncSubject
                    .removeListener(syncListener);
        }
    }

    @Override
    public void launchDetails(View sharedView,
                              String localPhotoId) {
        Fragment fragment = DetailsFragment
                .newInstance(localPhotoId);

        // transition for shared image
        TransitionSet transitionSet = new TransitionSet()
                .setOrdering(TransitionSet.ORDERING_TOGETHER)
                .addTransition(new ChangeBounds())
                .addTransition(new ChangeTransform())
                .addTransition(new ChangeImageTransform());
        fragment.setSharedElementEnterTransition(transitionSet);
        fragment.setSharedElementReturnTransition(transitionSet);

        // transitions for fragments
        fragment.setEnterTransition(new Fade());
        getSupportFragmentManager()
                .findFragmentById(R.id.fragmentContainer)
                .setReturnTransition(new Fade());

        getSupportFragmentManager()
                .beginTransaction()
                .addSharedElement(
                        sharedView, getString(R.string.shared_tag))
                .addToBackStack(null)
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

    @Override
    public void back() {
        onBackPressed();
    }

    private void updateUi() {

        if (syncInProgress) {
            messageContainer.setVisibility(View.VISIBLE);
            messageTextView.setText(R.string.updating_gallery);
            syncProgressBar.setVisibility(View.VISIBLE);
            actionTextView.setVisibility(View.INVISIBLE);

        } else if (hasUpdates) {
            messageContainer.setVisibility(View.VISIBLE);
            messageTextView.setText(R.string.update_available);
            syncProgressBar.setVisibility(View.INVISIBLE);
            actionTextView.setVisibility(View.VISIBLE);

        } else {
            messageContainer.setVisibility(View.GONE);
        }

    }

    private TaskListener<Boolean> hasUpdatesListener = res -> {
        if (res.getStatus() == Status.SUCCESS) {
            this.hasUpdates = res.getData();
            updateUi();
        }
    };

    private TaskListener<Boolean> syncListener = res -> {
        this.syncInProgress = res.getStatus() ==
                Status.IN_PROGRESS;
        if (res.getStatus() == Status.SUCCESS && res.getData()) {
            this.hasUpdates = false;
        }
        updateUi();
    };

}

