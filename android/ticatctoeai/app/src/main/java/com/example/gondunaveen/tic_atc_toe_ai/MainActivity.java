package com.example.gondunaveen.tic_atc_toe_ai;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    Button b1,b2,b3,b4,b5,b6,b7,b8,b9;
    Button [] bArray;
    public char activePlayer;
    public char[][] board = new char[3][3];
    public boolean ended;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b1 = (Button)findViewById(R.id.b00);
        b2 = (Button)findViewById(R.id.b01);
        b3 = (Button)findViewById(R.id.b02);
        b4 = (Button)findViewById(R.id.b10);
        b5 = (Button)findViewById(R.id.b11);
        b6 = (Button)findViewById(R.id.b12);
        b7 = (Button)findViewById(R.id.b20);
        b8 = (Button)findViewById(R.id.b21);
        b9 = (Button)findViewById(R.id.b22);

        bArray = new Button[]{b1,b2,b3,b4,b5,b6,b7,b8,b9};
        for(int p=0;p<3;p++) {
            for(int q=0;q<3;q++) {
                board[p][q] = ' ';
            }
        }
        for (Button b : bArray) {
            b.setOnClickListener(this);
        }
    }
    Board b = new Board();

    @Override
    public void onClick(View view) {
        Button v = (Button) view;
        int x = 0, y = 0;
        if (v.getId() == R.id.b00) {
            x = 0;
            y = 0;
        }
        if (v.getId() == R.id.b01) {
            x = 0;
            y = 1;
        }
        if (v.getId() == R.id.b02) {
            x = 0;
            y = 2;
        }
        if (v.getId() == R.id.b10) {
            x = 1;
            y = 0;
        }
        if (v.getId() == R.id.b11) {
            x = 1;
            y = 1;
        }
        if (v.getId() == R.id.b12) {
            x = 1;
            y = 2;
        }
        if (v.getId() == R.id.b20) {
            x = 2;
            y = 0;
        }
        if (v.getId() == R.id.b21) {
            x = 2;
            y = 1;
        }
        if (v.getId() == R.id.b22) {
            x = 2;
            y = 2;
        }

        Point user = new Point(x, y);
        b.placeAMove(user, 2);
        update_board();
        if (b.isGameOver())
            gameEnded();
        else {
            b.callMinimax(0, 1);
            b.placeAMove(b.returnBestMove(), 1);
            update_board();
            if(b.isGameOver())
                gameEnded();
        }
    }

    private void update_board() {
        for(int i=0 ;i< 3;i++) {
            for(int j=0;j<3;j++) {
                if (b.board[i][j] == 1){
                    bArray[3 * i + j].setText("X");
                    bArray[3 * i + j].setEnabled(false);
                }
                else if(b.board[i][j] == 2) {
                    bArray[3 * i + j].setText("O");
                    bArray[3 * i + j].setEnabled(false);
                }
            }
        }
    }

    private void gameEnded() {
        if(b.hasXWon())
            Toast.makeText(getApplicationContext(), "Computer won!!", Toast.LENGTH_SHORT).show();
        else if (b.hasOWon())
            Toast.makeText(getApplicationContext(), "You won!!", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(), "Game tied", Toast.LENGTH_SHORT).show();
    }

}

