package com.example.courtcounter;


import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    int score_A = 0;
    int score_B = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    /**

     Displays the given score for Team A.
     */
    public void team_A_3_score (View view) {
        score_A = (score_A + 3);
        display (score_A);
    }

    public void team_A_2_score (View view) {
        score_A = (score_A + 2);
        display (score_A);
    }

    public void team_A_1_score (View view) {
        score_A = (score_A + 1);
        display (score_A);
    }

    private void display(int number) {
        TextView quantityTextView = (TextView)
                findViewById(R.id.team_a_score);
        quantityTextView.setText("" + number);
    }


    /**

     Displays the given score for Team B.
     */

    public void team_B_3_score (View view) {
        score_B = (score_B + 3);
        displayForTeamB (score_B);
    }

    public void team_B_2_score (View view) {
        score_B = (score_B + 2);
        displayForTeamB (score_B);
    }

    public void team_B_1_score (View view) {
        score_B = (score_B + 1);
        displayForTeamB (score_B);
    }

    public void displayForTeamB(int score) {
        TextView scoreView = (TextView) findViewById(R.id.team_b_score);
        scoreView.setText(String.valueOf(score));
    }

    public void reset(View view){
        score_A=0;
        score_B=0;
        display(score_A);
        displayForTeamB(score_B);
    }



}