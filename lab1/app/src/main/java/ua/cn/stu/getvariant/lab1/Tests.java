package ua.cn.stu.getvariant.lab1;

import android.util.Log;

import androidx.fragment.app.Fragment;

import java.util.Arrays;

public class Tests {
    private String[][] test = null;
    private int quantityOfCorrectAnswr = 0;
    private int quantityOfWrongAnswr = 0;
    private int quantityOfElements = 6;

    Fragment fragment = null;
    int index = 0;
    public Tests(String language, Fragment fragment){
        this.fragment = fragment;
        test = parseTest(getData(language));
    }

    public String[] getData(String language){
        String[] array = null;
        switch(language){
            case "English":
                array = fragment.getResources().getStringArray(R.array.test_english);
                break;
            case "German":
                array = fragment.getResources().getStringArray(R.array.test_german);
                break;
        }
        return array;
    }

    public String[][] parseTest(String[] data){
        String[][] test = new String[data.length][quantityOfElements];
        for (int i = 0; i<data.length;i++){
            String[] tmp = data[i].split(",");
            for (int j = 0; j < quantityOfElements; j++){
                test[i][j]=tmp[j];
            }
        }
        return test;
    }

    public boolean hasNext(){
        if(index+1 >= test.length){
            return false;
        }
        return true;
    }

    public String getNextQuestion(){
        if (++index >= test.length){
            index = 0;
        }
        return test[index][0];
    }

    public String getQuestion(){
        return test[index][0];
    }

    public String getAnswer(){
        return test[index][quantityOfElements-1];
    }

    public boolean checkAnswer(String answer){
        boolean result = answer.equals(getAnswer());
        if (result) {
            quantityOfCorrectAnswr++;
        } else {
            quantityOfWrongAnswr++;
        }
        return result;
    }

    public String[] getAnswers(String question){
        String[] answers =  new String[4];
        for(int i = 0; i < test.length; i++){
            if(test[i][0].equals(question)) {
                for (int j = 1; j <= 4; j++) {
                    answers[j-1] = test[i][j];
                    Log.i("getAnswers_array","answer value is "+answers[i]);
                }
                break;
            }
        }
        return answers;
    }

    @Override
    public String toString() {
        return  "quantity of correct answers=" + quantityOfCorrectAnswr + "\n" +
                "quantity of wrong answers=" + quantityOfWrongAnswr;
    }

    public int getSize(){
        return test.length;
    }
}
