package ua.cn.stu.getvariant.lab3;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ActivityWithoutVariants extends AppCompatActivity implements TaskListener {
    public static final String EXTRA_TIME_RANGE = "RANGE";
    public static final String EXTRA_LANG = "LANG";

    private TextView displayQuestion = null;
    private TextView displayResult = null;
    private TextView inputAnswerField = null;
    private TextView viewTimer = null;

    Bundle savedInstanceState = null;

    private CountDownTimer timer = null;
    private int timeLimit = 0;

    Tests tests = null;

    private String[] testData;

    App app = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_without_variants);
        app = (App) getApplication();

        this.savedInstanceState = savedInstanceState;

        displayQuestion = findViewById(R.id.display_question_without_variants);
        displayResult = findViewById(R.id.display_result_without_variants);
        inputAnswerField = findViewById(R.id.answerInputField);
        viewTimer = findViewById(R.id.view_timer);

        String lang = getIntent().getStringExtra(EXTRA_LANG);
        timeLimit = Integer.parseInt(getIntent().getStringExtra(EXTRA_TIME_RANGE));


        String fileName = getString(R.string.file_test_name)+lang;
        TestFiles testFiles = new TestFiles(app);
        HandlerThread handlerThread = new HandlerThread("CustomThread");
        handlerThread.start();
        Handler looperHandler = new Handler(handlerThread.getLooper());
        Log.d("Thread", "Action send from the thread: "     + Thread.currentThread().getId());
        looperHandler.post(() -> {
            try {
                testFiles.readFile(getAssets().open(fileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            handlerThread.quitSafely();
        }


        findViewById(R.id.button_next_question).setOnClickListener(v -> {
            goToNextQuestion();
        });

        findViewById(R.id.button_check_answer).setOnClickListener(v -> {
            checkAnswer();
        });

        findViewById(R.id.button_finish_test).setOnClickListener(v->{
            finishTest();
        });
    }

    public void checkAnswer(){
        if(tests.checkAnswer(inputAnswerField.getText().toString())){
            showResult(
                    getString(R.string.show_result_correct),
                    getResources().getColor(R.color.green)
            );
        }else{
            showResult(
                    getString(R.string.show_result_wrong),
                    getResources().getColor(R.color.red)
            );
        }
    }

    public boolean goToNextQuestion(){
        if(tests.hasNext()) {
            String question = tests.getNextQuestion();
            displayQuestion(question);
            timer.cancel();
            timer.start();
            return true;
        }

        finishTest();
        return false;
    }

    public void showResult(String result, int color){
        displayResult.setText(result);
        displayResult.setTextColor(color);
        disableAnswerField();
    }

    public void disableAnswerField(){
        inputAnswerField.setEnabled(false);
    }

    public void enableAnswerField(){
        inputAnswerField.setEnabled(true);
        inputAnswerField.setText("");
    }

    public void finishTest(){
        timer.cancel();
        onBackPressed();
    }

    public void displayQuestion(String question){
        displayResult.setText("");
        displayQuestion.setText(question);
        enableAnswerField();
    }

    private void setTime(int time){
        viewTimer.setText(Integer.toString(time));
    }

    @Override
    public void onStart() {
        super.onStart();
        app.addListener(this);
        timer = new CountDownTimer(TimeUnit.SECONDS.toMillis(timeLimit), TimeUnit.SECONDS.toMillis(1)) {
            @Override
            public void onTick(long millisUntilFinished) {
                setTime((int)TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                checkAnswer();
                if(goToNextQuestion()){
                    timer.start();
                }
            }
        };
        timer.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        app.removeListener(this);
        timer.cancel();
    }

    @Override
    public void publishFileContext(String[][] data) {
        tests = new Tests(data);
        setTime(timeLimit);

        if (savedInstanceState == null){
            String question = tests.getQuestion();
            displayQuestion(question);
        }
    }
}