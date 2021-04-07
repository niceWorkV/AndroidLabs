package ua.cn.stu.getvariant.lab3;


import android.util.Log;
import java.io.IOException;
import java.io.InputStream;

public class TestFiles  {
    App app = null;

    public static final String TAG = TestFiles.class.getSimpleName();

    public TestFiles(App app){
        this.app=app;
    }

    public void readFile(InputStream fileName){
        byte[] buffer = null;
        InputStream is;
        try {
            is = fileName;
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


