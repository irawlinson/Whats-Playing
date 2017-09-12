package com.example.isaiahrawlinson.whatsplaying;

import android.graphics.Color;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TicTacToe extends AppCompatActivity {

    Button[][] buttonBoard = new Button[3][3];
    int[][] gameBoard = new int[3][3];
    int boardCapacity = 0;
    int wins=0, losses=0, ties=0;
    boolean done;
    int strategy = 0;

    TextView gamePrompt, scoreBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe);

        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle("Tic Tac Toe");

        initBoard(buttonBoard, gameBoard);
        gamePrompt = (TextView) findViewById(R.id.game_prompt);
        scoreBoard = (TextView) findViewById(R.id.score_board);

        for (int row=0; row<3; row++){
            for (int col=0; col<3; col++){
                buttonBoard[row][col].setOnClickListener(gameBoardListeners);
            }
        }
    }

    public View.OnClickListener gameBoardListeners = new View.OnClickListener() {

        @Override
        public void onClick (View v){
            moveO(v);

            checkFinished();
            if (done){
                wins++;
                gameOver();
                gamePrompt.setText(R.string.ttt_win_message);
            } else if (boardCapacity >= 9){
                ties++;
                gameOver();
                gamePrompt.setText(R.string.ttt_tie_message);
            } else computerMove2();
        }
    };

    public void moveO(View v){
        Button button = (Button) v;
        button.setTextColor(Color.rgb(0, 200, 0));
        button.setText("O");

        int row = Character.getNumericValue(v.getTag().toString().charAt(0));
        int col = Character.getNumericValue(v.getTag().toString().charAt(1));
        gameBoard[row][col] = 1;

        boardCapacity++;

        v.setEnabled(false);
    }

    public void moveX(View v){
        Button button = (Button) v;
        button.setTextColor(Color.rgb(200, 0, 0));
        button.setText("X");

        int row = Character.getNumericValue(v.getTag().toString().charAt(0));
        int col = Character.getNumericValue(v.getTag().toString().charAt(1));
        gameBoard[row][col] = -1;

        boardCapacity++;

        v.setEnabled(false);
    }

    public void resetClick(View v){
        initBoard(buttonBoard, gameBoard);
        gamePrompt.setText(R.string.ttt_start_message);
        boardCapacity = 0;
        done = false;
    }

    public void initBoard(Button[][] buttons, int[][] board){
        buttons[0][0] = (Button) findViewById(R.id.r0c0);
        buttons[0][1] = (Button) findViewById(R.id.r0c1);
        buttons[0][2] = (Button) findViewById(R.id.r0c2);

        buttons[1][0] = (Button) findViewById(R.id.r1c0);
        buttons[1][1] = (Button) findViewById(R.id.r1c1);
        buttons[1][2] = (Button) findViewById(R.id.r1c2);

        buttons[2][0] = (Button) findViewById(R.id.r2c0);
        buttons[2][1] = (Button) findViewById(R.id.r2c1);
        buttons[2][2] = (Button) findViewById(R.id.r2c2);

        for (int row=0; row<3; row++){
            for (int col=0; col<3; col++){
                buttons[row][col].setText("");
                buttons[row][col].setEnabled(true);

                board[row][col] = 0;
            }
        }
    }

    public void checkFinished(){
        int row = 0, col = 0, diag1, diag2;

        // Check rows
        for (int i=0; i<3; i++){
            for (int j=0; j<3; j++){
                row += gameBoard[i][j];
            }

            if (row == 3 || row == -3){
                done = true;
                break;
            } else row = 0;
        }

        // Check columns
        for (int i=0; i<3; i++){
            for (int j=0; j<3; j++){
                col += gameBoard[j][i];
            }

            if (col == 3 || col == -3){
                done = true;
                break;
            } else col = 0;
        }

        // Check diagonals
        diag1 = gameBoard[0][0] + gameBoard[1][1] + gameBoard[2][2];
        diag2 = gameBoard[0][2] + gameBoard[1][1] + gameBoard[2][0];

        if (diag1 == 3 || diag1 == -3 || diag2 == 3 || diag2 == -3)
            done = true;
    }

    public void setScoreBoard(){
        scoreBoard.setText("Wins: " + wins + " Losses: " + losses + " Ties: " + ties);
    }

    public void gameOver(){
        for (int i=0; i<3; i++){
            for (int j=0; j<3; j++){
                buttonBoard[i][j].setEnabled(false);
            }
        }

        setScoreBoard();
    }

    public void computerMove(){
        boolean myTurn = true;

        for (int i=0; i<3; i++){
            for (int j=0; j<3; j++){
                if (gameBoard[i][j] == 0 && myTurn){
                    myTurn = false;
                    moveX(buttonBoard[i][j]);
                }
            }
        }

        checkFinished();
        if (done){
            losses++;
            gameOver();
            gamePrompt.setText(R.string.ttt_lose_message);
        } else if (boardCapacity >= 9) {
            ties++;
            gameOver();
            gamePrompt.setText(R.string.ttt_tie_message);
        }
    }

    public void computerMove2(){

        defensive();
        /*if (strategy == 0){//defense
            strategy = (strategy + 1) % 2;

            offensive();
        } else {//offense
            strategy = (strategy + 1) % 2;

            defensive();
        }*/

        checkFinished();
        if (done){
            losses++;
            gameOver();
            gamePrompt.setText(R.string.ttt_lose_message);
        } else if (boardCapacity >= 9) {
            ties++;
            gameOver();
            gamePrompt.setText(R.string.ttt_tie_message);
        }

        /*
        * OFFENSIVE:
        *     - Start in the center or top right corner
        *         - Check rows/cols/diags for their sum
        *         - If the sum is negative, click another button in the row
        * DEFENSIVE:
        *     - Check where the player moved
        *         - Check rows/cols/diags for their sum
        *         - if the sum > 1, move in the row/col/diag
        * */
    }

    public void defensive(){
        int row = 0, col = 0, diag1, diag2;
        int bestRow, bestColumn, bestMove = 0;

        Point open = new Point(find2DIndex(gameBoard, 0));
        bestRow = open.x;
        bestColumn = open.y;

        //Check rows
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                row += gameBoard[i][j];

                if (row < bestMove && gameBoard[i][j] == 0){
                    bestMove = row;
                    bestRow = i;
                    bestColumn = j;
                }
            }
            row = 0;
        }

        //Check columns
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                col += gameBoard[j][i];
                if (col > bestMove && gameBoard[j][i] == 0){
                    bestMove = col;
                    bestRow = j;
                    bestColumn = i;
                }
            }
            col = 0;
        }

        //Check diagonals
        diag1 = gameBoard[0][0] + gameBoard[1][1] + gameBoard[2][2];
        diag2 = gameBoard[0][2] + gameBoard[1][1] + gameBoard[2][0];

        if (diag1 >= bestMove){
            for (int i=0; i<3; i++){
                if (gameBoard[i][i] == 0) {
                    bestRow = i;
                    bestColumn = i;
                }
            }
        }

        if (diag2 >= bestMove) {
            for (int i=0; i<3; i++) {
                if (gameBoard[i][2-i] == 0) {
                    bestRow = i;
                    bestColumn = 2-i;
                }
            }
        }

        moveX(buttonBoard[bestRow][bestColumn]);
    }

    public void offensive(){
        int row = 0, col = 0, diag1, diag2;
        boolean myTurn = true;
        int bestRow, bestCol;

        //Check rows
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                row += gameBoard[i][j];
            }

            if (row < 0) {
                for (int j = 0; j < 3; j++) {
                    if (gameBoard[i][j] == 0 && myTurn) {
                        moveX(buttonBoard[i][j]);
                        myTurn = false;
                    }
                }

            } else if (row <= 0) {
                for (int j = 0; j < 3; j++) {
                    if (gameBoard[i][j] == 0 && myTurn) {
                        moveX(buttonBoard[i][j]);
                        myTurn = false;
                    }
                }

            }
        }

        //Check columns
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                col += gameBoard[j][i];
            }

            if (col <= 0) {
                for (int j = 0; j < 3; j++) {
                    if (gameBoard[j][i] == 0 && myTurn) {
                        moveX(buttonBoard[j][i]);
                        myTurn = false;
                    }
                }

            } else if (col <= 0) {
                for (int j = 0; j < 3; j++) {
                    if (gameBoard[j][i] == 0 && myTurn) {
                        moveX(buttonBoard[j][i]);
                        myTurn = false;
                    }
                }

            }
        }

        //Check diagonals
        diag1 = gameBoard[0][0] + gameBoard[1][1] + gameBoard[2][2];
        diag2 = gameBoard[0][2] + gameBoard[1][1] + gameBoard[2][0];

        if (diag1 < 0){
            for (int i=0; i<3; i++){
                if (gameBoard[i][i] == 0 && myTurn) {
                    moveX(buttonBoard[i][i]);
                    myTurn = false;
                }
            }
        } else if (diag1 <= 0){
            for (int i=0; i<3; i++){
                if (gameBoard[i][i] == 0 && myTurn) {
                    moveX(buttonBoard[i][i]);
                    myTurn = false;
                }
            }
        }

        if (diag2 < 0) {
            for (int i=0; i<3; i++) {
                if (gameBoard[i][2-i] == 0 && myTurn) {
                    moveX(buttonBoard[i][2-i]);
                    myTurn = false;
                }
            }
        } else if (diag2 <= 0) {
            for (int i=0; i<3; i++) {
                if (gameBoard[i][2-i] == 0 && myTurn) {
                    moveX(buttonBoard[i][2-i]);
                    myTurn = false;
                }
            }
        }
    }

    private Point find2DIndex(int[][] array, int search) {

        for (int rowIndex = 0; rowIndex < array.length; rowIndex++ ) {
            int[] row = array[rowIndex];
            if (row != null) {
                for (int columnIndex = 0; columnIndex < row.length; columnIndex++) {
                    if (search == (row[columnIndex])) {
                        return new Point(rowIndex, columnIndex);
                    }
                }
            }
        }
        return null;
    }
}
