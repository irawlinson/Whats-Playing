package com.example.isaiahrawlinson.whatsplaying;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Games extends AppCompatActivity {

    static final Class[] gameClasses = new Class[]{Hangman.class, TicTacToe.class};
    static final String[] gameNames = new String[]{"Hangman", "Tic Tac Toe"};
    ListView gameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle("Games");

        gameList = (ListView) findViewById(R.id.game_list);

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, gameNames);

        gameList.setAdapter(adapter);

        gameList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                /*Toast toast = Toast.makeText(
                        getApplicationContext(),
                        "Ready to play " + gameNames[position] + "!",
                        Toast.LENGTH_SHORT);

                toast.show();*/

                Intent nextGame = new Intent(Games.this, gameClasses[position]);
                startActivity(nextGame);
            }
        });
    }
}
