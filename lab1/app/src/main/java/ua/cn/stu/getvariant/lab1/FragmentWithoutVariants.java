package ua.cn.stu.getvariant.lab1;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.concurrent.TimeUnit;

public class FragmentWithoutVariants extends Fragment implements FragmentTest {

    private TextView displayQuestion = null;
    private TextView displayResult = null;
    private TextView inputAnswerField = null;
    private TextView viewTimer = null;

    private CountDownTimer timer = null;
    private int timeLimit = 0;

    Tests tests = null;

    private static final String VALUE_TIME = "VALUE";
    private static final String VALUE_LANG = "LANG";

    public static FragmentWithoutVariants newInstance(String timeRange, String lang){
        Bundle args = new Bundle();
        args.putString(VALUE_TIME,timeRange);
        args.putString(VALUE_LANG,lang);
        FragmentWithoutVariants fragment = new FragmentWithoutVariants();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_test_without_variants,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        displayQuestion = view.findViewById(R.id.display_question_without_variants);
        displayResult = view.findViewById(R.id.display_result_without_variants);
        inputAnswerField = view.findViewById(R.id.answerInputField);
        viewTimer = view.findViewById(R.id.view_timer);

        String lang = getArguments().getString(VALUE_LANG);
        timeLimit = Integer.parseInt(getArguments().getString(VALUE_TIME));

        tests = new Tests(lang,this);
        setTime(timeLimit);

        if (savedInstanceState == null){
            String question = tests.getQuestion();
            displayQuestion(question);
        }

        view.findViewById(R.id.button_next_question).setOnClickListener(v -> {
            goToNextQuestion();
        });

        view.findViewById(R.id.button_check_answer).setOnClickListener(v -> {
            checkAnswer();
        });

        view.findViewById(R.id.button_finish_test).setOnClickListener(v->{
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
        Dialog dialog = new Dialog(tests.toString());
        dialog.show(getFragmentManager(),"dialog");
        timer.cancel();
        getActivity().onBackPressed();
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
        timer.cancel();
    }
}
