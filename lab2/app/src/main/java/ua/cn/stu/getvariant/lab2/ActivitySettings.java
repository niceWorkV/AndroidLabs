package ua.cn.stu.getvariant.lab2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Spinner;

public class ActivitySettings extends AppCompatActivity {
    private Spinner timeRange = null;
    private Spinner lang = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        timeRange = findViewById(R.id.selectTimeLimit);
        lang = findViewById(R.id.selectLang);


        findViewById(R.id.launch_with_variants).setOnClickListener(v->{
            Intent testWithVariants = new Intent(this, ActivityWithVariants.class);
            testWithVariants.putExtra(
                    ActivityWithVariants.EXTRA_TIME_RANGE,
                    timeRange.getSelectedItem().toString()
            );
            testWithVariants.putExtra(
                    ActivityWithVariants.EXTRA_LANG,
                    lang.getSelectedItem().toString()
            );
            startActivity(testWithVariants);
        });
        findViewById(R.id.launch_without_variants).setOnClickListener(v->{
            Intent testWithoutVariants = new Intent(this, ActivityWithoutVariants.class);
            testWithoutVariants.putExtra(
                    ActivityWithoutVariants.EXTRA_TIME_RANGE,
                    timeRange.getSelectedItem().toString()
            );
            testWithoutVariants.putExtra(
                    ActivityWithoutVariants.EXTRA_LANG,
                    lang.getSelectedItem().toString()
            );
            startActivity(testWithoutVariants);
        });

    }
}