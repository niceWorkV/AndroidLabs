package ua.cn.stu.getvariant.lab2;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;

public class TestFiles extends IntentService {

    public static final String ACTION_READ_FILE = "READ_FILE";
    public static final String EXTRA_FILE = "FILE";
    public static final String TAG = TestFiles.class.getSimpleName();

    public TestFiles() {
        super(TestFiles.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.i(TAG,"Service TestFiles is started");
        String action = intent.getAction();
        Log.i(TAG,"action is "+action);
        if(action.equals(ACTION_READ_FILE)) {
            String fileName = intent.getStringExtra(EXTRA_FILE);
            readFile(fileName);
        }
    }

    public void readFile(String fileName){
        App app = (App) getApplication();
        Log.i("File",fileName);
        byte[] buffer = null;
        InputStream is;
        try {
            is = getAssets().open(fileName);
            int size = is.available();
            buffer = new byte[size];
            is.read(buffer);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String data = new String(buffer);
        String lines[] = data.split("\\n");
        for(String str : lines){
            Log.i(TAG,str);
        }
        app.publishData(parseTest(lines));
    }

    public String[][] parseTest(String[] data){
        int quantityOfElements = 6;

        String[][] test = new String[data.length][quantityOfElements];
        for (int i = 0; i<data.length;i++){
            String[] tmp = data[i].split(",");
            for (int j = 0; j < quantityOfElements; j++){
                test[i][j]=tmp[j];
            }
        }
        return test;
    }
}


