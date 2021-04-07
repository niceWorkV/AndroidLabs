package ua.cn.stu.getvariant.lab2;

import android.app.Application;
import android.os.Handler;

import java.util.HashSet;
import java.util.Set;

public class App extends Application {
    private Set<TaskListener> listeners = new HashSet<>();
    private Handler handler = new Handler();

    public void addListener(TaskListener listener){
        this.listeners.add(listener);
    }

    public void removeListener(TaskListener listener){
        this.listeners.remove(listener);
    }

    public void publishData(String[][] data){
        handler.post(()->{
            for(TaskListener task: listeners){
                task.publishFileContext(data);
            }
        });
    }
}
