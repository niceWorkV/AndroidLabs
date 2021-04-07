package ua.cn.stu.getvariant.lab2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class ActivityWithVariants extends AppCompatActivity implements TaskListener {
    public static final String EXTRA_TIME_RANGE = "RANGE";
    public static final String EXTRA_LANG = "LANG";

    private TextView displayQuestion = null;
    private TextView displayResult = null;
    private Button finishTestButton = null;
    private Button[] answerButtons;
    private TextView viewTimer = null;

    Bundle savedInstanceState = null;

    private CountDownTimer timer = null;
    private int timeLimit = 0;

    Tests tests = null;

    App app = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        setContentView(R.layout.activity_with_variants);
        app = (App) getApplication();

        displayQuestion = findViewById(R.id.display_question_with_variants);
        displayResult = findViewById(R.id.display_result_with_variants);
        finishTestButton = findViewById(R.id.button_finish_test);
        viewTimer = findViewById(R.id.view_variant_timer);
        answerButtons = new Button[]{
                findViewById(R.id.button_asw1),
                findViewById(R.id.button_asw2),
                findViewById(R.id.button_asw3),
                findViewById(R.id.button_asw4)
        };

        String lang = getIntent().getStringExtra(EXTRA_LANG);
        timeLimit = Integer.parseInt(getIntent().getStringExtra(EXTRA_TIME_RANGE));

        String fileName = getString(R.string.file_test_name)+lang;

        Intent intent = new Intent(this, TestFiles.class);
        intent.setAction(TestFiles.ACTION_READ_FILE);
        intent.putExtra(TestFiles.EXTRA_FILE,fileName);
        startService(intent);
//        Log.i("Array", new Integer(testData.length).toString());

        findViewById(R.id.button_next_question).setOnClickListener(v -> {
            goToNextQuestion();
        });

        for(Button button: answerButtons){
            button.setOnClickListener(v ->{
                if(tests.checkAnswer(button.getText().toString())){
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
            });
        }

        finishTestButton.setOnClickListener(v ->{
            finishTest();
        });
    }


    private void setTime(int time){
        viewTimer.setText(Integer.toString(time));
    }

    public boolean goToNextQuestion() {
        if (tests.hasNext()) {
            String question = tests.getNextQuestion();
            displayQuestion(question, tests.getAnswers(tests.getQuestion()));
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
        disableAnswerButtons();
    }

    public void disableAnswerButtons(){
        for(Button button: answerButtons){
            button.setEnabled(false);
        }
        timer.cancel();
    }

    public void enableAnswerButtons(){
        for(Button button: answerButtons){
            button.setEnabled(true);
        }
    }

    public void finishTest(){
//        Dialog dialog = new Dialog(tests.toString());
        onBackPressed();
    }

    public void displayQuestion(String question, String[] answers){
        displayResult.setText("");
        displayQuestion.setText(question);
        for (int i = 0; i < answerButtons.length; i++) {
            answerButtons[i].setText(answers[i]);
        }
        enableAnswerButtons();
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
            displayQuestion(question,tests.getAnswers(question));
        }
    }
}