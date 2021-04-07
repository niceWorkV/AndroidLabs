package ua.cn.stu.getvariant.lab2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null){
            Intent settings = new Intent(this, ActivitySettings.class);
            startActivity(settings);
//            FragmentSettings settings = new FragmentSettings();
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .add(R.id.frame_container,settings).commit();
        }
    }
}