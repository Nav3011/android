package com.example.gondunaveen.tactical;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Random;


public class player_vs_computer extends AppCompatActivity implements View.OnClickListener {

    Button b1,b2,b3,b4,b5,b6,b7,b8,b9;
    Button [] bArray;
    public char activePlayer;
    public char[][] board = new char[3][3];
    public boolean ended;
    public static final Random RANDOM = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_vs_computer);

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
        newGame();
    }

    @Override
    public void onClick(View view) {
       Button v = (Button) view;
       int x=0, y = 0;
        if (v.getId() == R.id.b00) {x=0;y=0;}
        if (v.getId() == R.id.b01) {x=0;y=1;}
        if (v.getId() == R.id.b02) {x=0;y=2;}
        if (v.getId() == R.id.b10) {x=1;y=0;}
        if (v.getId() == R.id.b11) {x=1;y=1;}
        if (v.getId() == R.id.b12) {x=1;y=2;}
        if (v.getId() == R.id.b20) {x=2;y=0;}
        if (v.getId() == R.id.b21) {x=2;y=1;}
        if (v.getId() == R.id.b22) {x=2;y=2;}
        v.setText("X");
        v.setEnabled(false);
       char win = play(x,y);
        if(win!=' ') {
            gameEnded(win);
        }
        else{
         win = computer();
         update_board();
         if (win!=' ')
             gameEnded(win);
        }
        //display();
    }

    public char computer() {
        if (!ended) {
            int x = -1;
            int y = -1;
            do {
                x = RANDOM.nextInt(3);
                y = RANDOM.nextInt(3);
            }while (board[x][y]!=' ');
            board[x][y] = activePlayer;
            changePlayer();
        }
        return checkEnd();
    }

    public boolean isEnded() {
        return ended;
    }

    public char play(int x, int y) {
        //Toast.makeText(getApplicationContext(), "X: "+x+", Y:"+y, Toast.LENGTH_SHORT).show();
        if (!ended && board[x][y] == ' ') {
            board[x][y] = activePlayer;

            changePlayer();
        }
        return checkEnd();
    }

    public void changePlayer() {
        if (activePlayer == 'X')
            activePlayer='O';
        else
            activePlayer='X';
    }

    public char element(int x, int y) {
        return board[x][y];
    }

    public void newGame() {
        for(int i=0;i<3;i++) {
            for(int j=0;j<3;j++) {
                board[i][j]=' ';
            }
        }
        update_board();
        activePlayer='X';
        ended = false;
    }

    public void update_board() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 'X') {
                    bArray[3*i+j].setText("X");
                    bArray[3*i+j].setEnabled(false);
                }
                if (board[i][j] == 'O') {
                    bArray[3 * i + j].setText("O");
                    bArray[3 * i + j].setEnabled(false);
                }
                if (board[i][j] == ' ')
                    bArray[3*i+j].setText("");
            }
        }
    }

    public char checkEnd() {
        for(int i=0;i<3;i++){
            if (element(i,0)!=' ' && element(i,0) == element(i,1) && element(i,1) == element(i,2)) {
                ended = true;
                return element(i,0);
            }
            if (element(0,i)!=' ' && element(0,i) == element(1,i) && element(1,i) == element(2,i)) {
                ended = true;
                return element(0,i);
            }
        }
        if (element(0,0)!=' ' && element(0,0) == element(1,1) && element(1,1) == element(2,2)){
            ended = true;
            return element(0,0);
        }
        if (element(2,0)!=' ' && element(2,0) == element(1,1) && element(1,1) == element(0,2)) {
            ended = true;
            return element(2,0);
        }
        for(int i=0;i<3;i++) {
            for(int j=0;j<3;j++) {
                if(board[i][j] == ' ')
                    return ' ';
            }
        }
        return 'T';
    }


    public void gameEnded(char c) {
        if(c == 'T')
                Toast.makeText(getApplicationContext(),"Game Ended. Tie" , Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(),"Game Ended...."+c+" wins...!!!",Toast.LENGTH_SHORT).show();

        /*new AlertDialog.Builder(this).setTitle("Tic Tac Toe").setMessage(msg).setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                newGame();
            }
        }).show();*/
    }

}

