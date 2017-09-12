package com.example.isaiahrawlinson.whatsplaying;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.Random;

public class Hangman extends AppCompatActivity {

    char guess;
    String answer, answerHidden;
    String message_top, message_bottom;
    boolean done;
    int guessesUsed, guessesMax;
    TextView word, display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hangman);
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle("Hangman");

        word = (TextView) findViewById(R.id.word);
        display = (TextView) findViewById(R.id.game_display);
        newGame();
    }

    // onClick method. This will get the text from the button.
    public void letterClick(View v){
        guess = ((Button) v).getText().toString().toLowerCase().charAt(0);//guess.charAt(0);

        if (!done) {
            search(guess);
            word.setText(message_top);
            display.setText(message_bottom);
            v.setVisibility(View.INVISIBLE);
        }
    }


    public void search(char letter){
        int index = answer.indexOf(letter);
        guessesUsed++;

        while (index != -1){
            answerHidden = replace(answerHidden, index, letter);
            index = answer.indexOf(letter, index+1);
        }

        if (answerHidden.equals(answer)) {
            message_top = getResources().getString(R.string.hangman_win_message);
            message_bottom = "The word was: " + answer + ". you used " + guessesUsed + " guesses.";
            done = true;
        } else if (guessesUsed < guessesMax){
            message_top = answerHidden;
            message_bottom = "used " + guessesUsed + " of " + guessesMax + " guesses";
        } else {
            message_top = getResources().getString(R.string.hangman_lose_message);
            message_bottom = "The word was: " + answer + ". You used " + guessesUsed + " guesses.";
            done = true;
        }
    }


    public void resetClick(View v){
        newGame();
        resetButtons();
        //recreate();
    }

    public void newGame(){
        answer = generateWord();
        answerHidden = answer;
        Integer len = answer.length();

        guessesMax = len + 5;
        guessesUsed = 0;

        done = false;

        for (int i = 0; i < len; i++) {
            answerHidden = replace(answerHidden, i, '-');
        }

        message_top = answerHidden;
        message_bottom = "consists of " + len + " letters";

        word.setText(message_top);
        display.setText(message_bottom);
    }

    /*
     * Takes a string, index, and character
     * Returns a string with the character at the specified index
     * of the word replaced with the desired character.
    */
    public String replace(String str, int index, char replace){
        if (str.isEmpty()) {
            return str;
        }else if (index < 0 || index >= str.length()){
            return str;
        }
        char[] chars = str.toCharArray();
        chars[index] = replace;
        return String.valueOf(chars);
    }

    // Generates an array of Strings (words) and returns a random string from the array.
    private String generateWord(){
        String [] words = getResources().getStringArray(R.array.word_list);
        Random r = new Random();
        int index = r.nextInt(words.length);
        return words[index];
    }

    public void resetButtons(){
        Button[] alphabet = new Button[26];

        alphabet[0] = (Button) findViewById(R.id.button_a);
        alphabet[1] = (Button) findViewById(R.id.button_b);
        alphabet[2] = (Button) findViewById(R.id.button_c);
        alphabet[3] = (Button) findViewById(R.id.button_d);
        alphabet[4] = (Button) findViewById(R.id.button_e);
        alphabet[5] = (Button) findViewById(R.id.button_f);
        alphabet[6] = (Button) findViewById(R.id.button_g);
        alphabet[7] = (Button) findViewById(R.id.button_h);
        alphabet[8] = (Button) findViewById(R.id.button_i);
        alphabet[9] = (Button) findViewById(R.id.button_j);
        alphabet[10] = (Button) findViewById(R.id.button_k);
        alphabet[11] = (Button) findViewById(R.id.button_l);
        alphabet[12] = (Button) findViewById(R.id.button_m);
        alphabet[13] = (Button) findViewById(R.id.button_n);
        alphabet[14] = (Button) findViewById(R.id.button_o);
        alphabet[15] = (Button) findViewById(R.id.button_p);
        alphabet[16] = (Button) findViewById(R.id.button_q);
        alphabet[17] = (Button) findViewById(R.id.button_r);
        alphabet[18] = (Button) findViewById(R.id.button_s);
        alphabet[19] = (Button) findViewById(R.id.button_t);
        alphabet[20] = (Button) findViewById(R.id.button_u);
        alphabet[21] = (Button) findViewById(R.id.button_v);
        alphabet[22] = (Button) findViewById(R.id.button_w);
        alphabet[23] = (Button) findViewById(R.id.button_x);
        alphabet[24] = (Button) findViewById(R.id.button_y);
        alphabet[25] = (Button) findViewById(R.id.button_z);

        for (int i=0; i<26; i++)
            alphabet[i].setVisibility(View.VISIBLE);
    }
}
