package ua.cn.stu.getvariant.lab2;

import android.util.Log;

import androidx.fragment.app.Fragment;

public class Tests {
    private String[][] test = null;
    private int quantityOfCorrectAnswr = 0;
    private int quantityOfWrongAnswr = 0;
    private int quantityOfElements = 6;

    Fragment fragment = null;
    int index = 0;
    public Tests(String[][] data){
        setTest(data);
    }

    private void setTest(String[][] data) {
        test = data;
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
        Log.i("Answer","your choose is = "+answer);
        Log.i("Answer","correct answer = "+getAnswer());
        boolean result = answer.equals(getAnswer());
        if (result) {
            quantityOfCorrectAnswr++;
        } else {
            quantityOfWrongAnswr++;
        }
        Log.i("Answer","check result = "+result);
        return result;
    }

    public String[] getAnswers(String question){
        String[] answers =  new String[4];
        for(int i = 0; i < test.length; i++){
            if(test[i][0].equals(question)) {
                for (int j = 1; j <= 4; j++) {
                    answers[j-1] = test[i][j];
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
