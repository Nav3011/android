package com.example.gondunaveen.tactical;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void player_vs_computer(View view){
        Intent intent = new Intent(this, player_vs_computer.class);
        if(intent.resolveActivity(getPackageManager())!=null)
            startActivity(intent);
    }

    public void player_vs_player(View view) {
        Intent intent = new Intent(this, player_vs_player.class);
        if(intent.resolveActivity(getPackageManager())!=null)
            startActivity(intent);
    }
}
