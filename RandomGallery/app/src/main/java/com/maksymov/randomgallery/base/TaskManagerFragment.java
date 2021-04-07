package com.maksymov.randomgallery.base;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TaskManagerFragment extends Fragment {

    public static final String TAG =
            TaskManagerFragment.class.getSimpleName();

    private ExecutorService executorService;

    private Handler handler =
            new Handler(Looper.getMainLooper());

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // помічаємо фрагмент як такий, що має "виживати"
        // при зміні конфігурації
        setRetainInstance(true);

        executorService = Executors.newSingleThreadExecutor();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // перериваємо виконання всіх задач
        executorService.shutdownNow();
    }

    public <T> TaskSubject<T> submitTask(Callable<T> callable) {
        TaskSubjectImpl<T> taskSubject = new TaskSubjectImpl<>();
        taskSubject.future = executorService.submit(() -> {
            try {
                T result = callable.call();
                taskSubject.setResult(Result.success(result));
            } catch (Exception e) {
                taskSubject.setResult(Result.error(e));
            }
        });
        return taskSubject;
    }


    /**
     * Внутрішня реалізація TaskSubject
     */
    class TaskSubjectImpl<T> implements TaskSubject<T> {

        // тут зберігається поточний стан та результат
        // виконання задачі
        private Result<T> result = Result.inProgress();

        // набір слухачів задачі
        // LinkedHashSet зберагіє елементи в порядку додавання
        private volatile Set<TaskListener<T>> listeners =
                new LinkedHashSet<>();

        // об'єкт Future, в даному випадку він зберігається для
        // можливості відміни виконання задачі
        private Future<?> future;

        @Override
        public synchronized void
        addListener(TaskListener<T> listener) {
            this.listeners.add(listener);
            listener.onResults(result);
        }

        @Override
        public synchronized void
        removeListener(TaskListener<T> listener) {
            this.listeners.remove(listener);
        }

        @Override
        public void cancel() {
            if (!future.isCancelled()) {
                // відразу сповіщуємо про відміну задачі
                setResult(Result.error(new
                        CancelledException()));
                future.cancel(true);
            }
        }

        void setResult(Result<T> result) {
            if (isMainThread()) {
                // знаходимось в головному потоці, немає
                // потреби використовувати Handler
                doSetResult(result);
            } else {
                handler.post(() -> doSetResult(result));
            }
        }

        private void doSetResult(Result<T> result) {
            if (this.result.getStatus() != Status.IN_PROGRESS) {
                // якщо результат задачі вже встановлено, то
                // ігноруємо подальші спроби зміни результату
                return;
            }

            this.result = result;
            List<TaskListener<T>> listeners =
                    new ArrayList<>(this.listeners);
            for (TaskListener<T> listener : listeners) {
                listener.onResults(result);
            }
        }

        private boolean isMainThread() {
            return Thread.currentThread().getId()
                    == Looper.getMainLooper().getThread().getId();
        }
    }
}
