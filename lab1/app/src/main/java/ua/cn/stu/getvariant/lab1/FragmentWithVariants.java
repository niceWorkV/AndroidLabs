package ua.cn.stu.getvariant.lab1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.concurrent.TimeUnit;

public class FragmentWithVariants extends Fragment implements FragmentTest {

    private TextView displayQuestion = null;
    private TextView displayResult = null;
    private Button finishTestButton = null;
    private Button[] answerButtons;
    private TextView viewTimer = null;

    private CountDownTimer timer = null;
    private int timeLimit = 0;

    Tests tests = null;

    private static final String VALUE_TIME = "VALUE";
    private static final String VALUE_LANG = "LANG";

    public static FragmentWithVariants newInstance(String timeRange, String lang){
        Bundle args = new Bundle();
        args.putString(VALUE_TIME,timeRange);
        args.putString(VALUE_LANG,lang);
        FragmentWithVariants fragment = new FragmentWithVariants();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_test_with_variants,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        displayQuestion = view.findViewById(R.id.display_question_with_variants);
        displayResult = view.findViewById(R.id.display_result_with_variants);
        finishTestButton = view.findViewById(R.id.button_finish_test);
        viewTimer = view.findViewById(R.id.view_variant_timer);
        answerButtons = new Button[]{
                view.findViewById(R.id.button_asw1),
                view.findViewById(R.id.button_asw2),
                view.findViewById(R.id.button_asw3),
                view.findViewById(R.id.button_asw4)
        };

        String lang = getArguments().getString(VALUE_LANG);
        timeLimit = Integer.parseInt(getArguments().getString(VALUE_TIME));

        tests = new Tests(lang,this);
        setTime(timeLimit);

        if (savedInstanceState == null){
            String question = tests.getQuestion();
            displayQuestion(question,tests.getAnswers(question));
        }

        view.findViewById(R.id.button_next_question).setOnClickListener(v -> {
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
        Dialog dialog = new Dialog(tests.toString());
        dialog.show(getFragmentManager(),"dialog");
        getActivity().onBackPressed();
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
        timer.cancel();
    }
}
