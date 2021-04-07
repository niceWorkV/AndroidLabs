package ua.cn.stu.getvariant.lab4.logger;

import android.util.Log;

public class AndroidLogger implements Logger {

    public static final String TAG = AndroidLogger.class.getSimpleName();
    @Override
    public void e(Throwable e) {
        Log.e(TAG,"Erre!",e);
    }
}
