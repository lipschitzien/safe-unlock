package com.example.unlock3;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class RaspActivity extends AppCompatActivity implements View.OnClickListener{

    //private Button backMain, recherche;
    private ListView listView;
    private ImageView backMain;

    private int userId;

    //private MyDBhelper myDBhelper;
    //private ArrayList<PersonRasp> personRasps;
    private MyAdapter2 myAdapter;

    private ArrayList<PersonRasp> resultList;

    private String ss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        this.init();
    }

    //chercher les contenus de bdd, afficher les contenus dans le listview
    private void init(){

        this.listView = findViewById(R.id.listview);
        this.backMain = findViewById(R.id.backMain);
        //this.recherche = findViewById(R.id.recherche);
        this.backMain.setOnClickListener(this);
        //this.recherche.setOnClickListener(this);


        this.resultList = new ArrayList<>();

        ss = getIntent().getStringExtra("csv");
        userId = getIntent().getIntExtra("userId", -1);

        try {
            String[] lines = ss.split(";");

            for (int i = 1; i < lines.length-1; i++) {
                String[] items = lines[i].split(",");

                PersonRasp person = new PersonRasp();

                if (!items[0].equals("FIN")) {
                    person.setId(Integer.parseInt(items[0]));
                    person.setPersonname(items[1]);
                    person.setResultat(items[2]);
                    person.setDate(items[3]);

                    this.resultList.add(person);
                }
            }
        } catch (NumberFormatException e) {
            // Handle the case where the data is not valid
            e.printStackTrace();
        }

        myAdapter = new MyAdapter2(RaspActivity.this, this.resultList);
        listView.setAdapter(myAdapter);


    }


        //??????
    //quand les données reviennent, il excécute automatiquement
    //pour recevoir les données reviennent
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == 2){
            //c_à_d excécute normal, il ajoute une historique dans le bbd, il faut renouveller listview
            init();
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.backMain){
            //c'est suffit de fermer cette page Activity
            //ss="";
            //finish();
            Intent intent = new Intent(RaspActivity.this, MainActivity.class);
            intent.putExtra("userId", userId);
            //startActivity(intent);
            startActivityForResult(intent, 1);
        }
        /*
        else if(view.getId() == R.id.recherche){
            System.out.println("cliqué sur btn recherche");
        }
        
         */
    }
}