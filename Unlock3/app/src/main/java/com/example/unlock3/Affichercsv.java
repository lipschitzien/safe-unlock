package com.example.unlock3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Affichercsv extends AppCompatActivity implements View.OnClickListener{

    private TextView t1;
    private String ss;

    private ImageView backMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affichercsv);

        t1 = findViewById(R.id.csv1);
        ss = getIntent().getStringExtra("csv");
        this.backMain = findViewById(R.id.backMain);
        this.backMain.setOnClickListener(this);
        t1.setText(ss);



    }


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.backMain){
            t1.setText(null);
            //c'est suffit de fermer cette page Activity
            finish();
        }
    }
}