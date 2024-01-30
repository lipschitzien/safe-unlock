package com.example.unlock3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class RecordActivity extends AppCompatActivity implements View.OnClickListener {

    //private Button backMain, recherche;
    private ListView listView;
    private ImageView backMain;

    private MyDBhelper myDBhelper;
    private MyAdapter myAdapter;

    private ArrayList<Note> resultList;

    private int userId;

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

        if(resultList != null){
            resultList.clear();
        }

        Intent intent = getIntent();
        this.userId = intent.getIntExtra("userId", -1);

        myDBhelper = new MyDBhelper(RecordActivity.this, "note.db", null, 1);
        this.resultList = myDBhelper.queryNoteByUser(userId);

        myAdapter = new MyAdapter(RecordActivity.this, this.resultList);

        listView.setAdapter(myAdapter);

        /*
        try {
            myDBhelper = new MyDBhelper(RecordActivity.this, "note.db", null, 1);
            SQLiteDatabase db = myDBhelper.getReadableDatabase();

            //myDBhelper = new MyDBhelper(TicTacToe.this, "data2.db", null, 1);
            //SQLiteDatabase db = myDBhelper.getWritableDatabase();

            //Cursor: premier ligne
            Cursor cursor = db.query("note", new String[]{"id", "content", "note_time"},null,null,null,null,null, null);


            if (cursor.moveToFirst()) {

                this.historique.setText("");
                this.historique.append("id： "+cursor.getString(0) + " "+cursor.getString(1) + " "+cursor.getString(2) + "\n");

                while (cursor.moveToNext()){
                    this.historique.append("id： "+cursor.getString(0) + " "+cursor.getString(1) + " "+cursor.getString(2) + "\n");
                }
            } else {
                // s'il n'y a pas de donnée
                this.historique.setText("Aucun historique");
            }

            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

         */


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
            finish();
        }
        /*
        else if(view.getId() == R.id.recherche){
            System.out.println("cliqué sur btn recherche");
        }

         */
    }
}