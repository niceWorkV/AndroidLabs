package ua.cn.stu.getvariant.lab1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null){
            FragmentSettings settings = new FragmentSettings();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.frame_container,settings).commit();
        }
    }
}