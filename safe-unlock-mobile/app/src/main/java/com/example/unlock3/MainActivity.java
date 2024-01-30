package com.example.unlock3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity{
    //UI Element
    Button btnUp;
    //Button btnDown;

    Button btnBloquer, btnDebloquer;

    Button btn3, btn4;
    //EditText txtAddress;
    //TextView t1;

    //Socket myAppSocket = null;

    MyDBhelper myDBhelper;
    String btnClique;
    private TextView titre;
    private ImageButton userEdit, exit;

    public static String wifiModuleIp = "";
    public static int wifiModulePort = 0;
    public static String CMD = "0";

    //chemin de dossier privé
    private File privateDir;

    private int userId;
    //private User user;

    private String iPandPort;

    private String csv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        csv="";


        myDBhelper = new MyDBhelper(MainActivity.this, "note.db", null, 1);
        this.btnClique = "";
        //this.t1 = findViewById(R.id.t1);
        this.titre = findViewById(R.id.titre);
        this.userEdit = findViewById(R.id.userEdit);
        this.exit = findViewById(R.id.exit);

        setResult(RESULT_OK);
        Intent intent = getIntent();
        this.userId = intent.getIntExtra("userId", -1);

        //this.user = myDBhelper.queryUserbyId(this.userId); faux faux faux
        User user = myDBhelper.queryUserbyId(this.userId);

        this.iPandPort = user.getHostname();

        //pour tester
        //this.iPandPort = "192.168.1.47:21567";

        String username = user.getUserName();
        this.titre.setText(username + ", bienvenu!");

        this.userEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //aller à la page compte
                Intent intent = new Intent(MainActivity.this, UserActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        btnUp = (Button) findViewById(R.id.btnUp);
        //btnDown = (Button) findViewById(R.id.btnDown);

        //btnBloquer = (Button) findViewById(R.id.btnBloquer);
        //btnDebloquer = (Button) findViewById(R.id.btnDebloquer);

        btn3 = (Button) findViewById(R.id.btn3);
        btn4 = (Button) findViewById(R.id.btn4);
        //btn5 = (Button) findViewById(R.id.btn5);

        //txtAddress = (EditText) findViewById(R.id.ipAddress);


        //btenir le chemin du dossier privé
        privateDir = getExternalFilesDir(null);

        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnClique = "Ouvert";
                //System.out.println("cliqué btn up");
                try{
                    getIPandPort();
                    CMD = "Open";
                    Socket_AsyncTask cmd_increase_servo = new Socket_AsyncTask();
                    cmd_increase_servo.execute();

                    if(btnClique != null){
                        //ajouter dans le bbd
                        myDBhelper = new MyDBhelper(MainActivity.this, "note.db", null, 1);
                        //int noteId = myDBhelper.insertNote(btnClique, userId);
                        boolean flag = myDBhelper.insertNote(btnClique, userId);

                        //if(noteId != -1){
                        if(flag){
                            //si réussi, set le résultat en 2
                            setResult(2);
                            //myDBhelper.insertNoteUser(noteId, userId);

                            showMsg("ajouté dans l'historique");
                        }else{
                            showMsg("fail");
                        }
                    }
                }
                catch (RuntimeException e){
                    showMsg(e.getMessage());
                }

            }
        });

        /*
        btnBloquer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnClique = "Desavtiver";
                try{
                    getIPandPort();
                    CMD = "Close";
                    Socket_AsyncTask cmd_increase_servo = new Socket_AsyncTask();
                    cmd_increase_servo.execute();

                    if(btnClique != null){
                        ///ajouter dans le bbd
                        myDBhelper = new MyDBhelper(MainActivity.this, "note.db", null, 1);
                        //int noteId = myDBhelper.insertNote(btnClique, userId);
                        boolean flag = myDBhelper.insertNote(btnClique, userId);

                        //if(noteId != -1){
                        if(flag){
                            //si réussi, set le résultat en 2
                            setResult(2);
                            //myDBhelper.insertNoteUser(noteId, userId);
                            showMsg("ajouté dans l'historique");
                        }else{
                            showMsg("fail");
                        }
                    }

                }
                catch (RuntimeException e){
                    showMsg(e.getMessage());
                }
            }
        });

        btnDebloquer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnClique = "Activer";
                try{
                    getIPandPort();
                    CMD = "avtive";
                    Socket_AsyncTask cmd_increase_servo = new Socket_AsyncTask();
                    cmd_increase_servo.execute();

                    if(btnClique != null){
                        ///ajouter dans le bbd
                        myDBhelper = new MyDBhelper(MainActivity.this, "note.db", null, 1);
                        //int noteId = myDBhelper.insertNote(btnClique, userId);
                        boolean flag = myDBhelper.insertNote(btnClique, userId);

                        //if(noteId != -1){
                        if(flag){
                            //si réussi, set le résultat en 2
                            setResult(2);
                            //myDBhelper.insertNoteUser(noteId, userId);
                            showMsg("ajouté dans l'historique");
                        }else{
                            showMsg("fail");
                        }
                    }

                }
                catch (RuntimeException e){
                    showMsg(e.getMessage());
                }
            }
        });

        */
        /*
        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnClique = "Fermé";
                //System.out.println("cliqué btn down");
                try{
                    getIPandPort();
                    CMD = "Close";
                    Socket_AsyncTask cmd_increase_servo = new Socket_AsyncTask();
                    cmd_increase_servo.execute();

                    if(btnClique != null){
                        ///ajouter dans le bbd
                        myDBhelper = new MyDBhelper(MainActivity.this, "note.db", null, 1);
                        //int noteId = myDBhelper.insertNote(btnClique, userId);
                        boolean flag = myDBhelper.insertNote(btnClique, userId);

                        //if(noteId != -1){
                        if(flag){
                            //si réussi, set le résultat en 2
                            setResult(2);
                            //myDBhelper.insertNoteUser(noteId, userId);
                            showMsg("ajouté dans l'historique");
                        }else{
                            showMsg("fail");
                        }
                    }
                }
                catch (RuntimeException e){
                    showMsg(e.getMessage());
                }

            }
        });*/
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RecordActivity.class);
                //startActivityForResult(intent, 1);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        //événement de clic sur un bouton
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnClique = "lire";
                //System.out.println("cliqué btn csv");
                try{
                    getIPandPort();
                    // envoyer une commande
                    CMD = "lire";
                    Socket_AsyncTask task = new Socket_AsyncTask();
                    task.execute();

                    try {
                        Object response = task.get();

                        //convertir les données renvoyées par Raspberry Pi en String
                        String responseString = String.valueOf(response);

                        //enregistrer les données renvoyées par Raspberry Pi dans un fichier CSV
                        writeCsvToFile(responseString);

                        csvAfficher("rasp.csv");
                        //Log.i("==", "ccc"+csv);
                        //Intent intent = new Intent(MainActivity.this, TestActivity.class);

                        Intent intent = new Intent(MainActivity.this, RaspActivity.class);
                        //Intent intent = new Intent(MainActivity.this, Affichercsv.class);
                        intent.putExtra("csv", csv);
                        intent.putExtra("userId", userId);
                        startActivity(intent);


                        Log.i("===", csv);

                    } catch (Exception e) {
                        //traitement des exceptions
                        Log.e("MainActivity", "Error getting response from Raspberry Pi", e);
                    }
                }
                catch (RuntimeException e){
                    showMsg(e.getMessage());
                }

            }
        });

        /*
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnClique = "btn5";
                //System.out.println("lire csv");

                String[] lines = readCsvFromFile("rasp.csv").split("\n");

                //afficher le contenu du fichier CSV rasp.csv ligne par ligne dans logcat
                for (String line : lines) {
                    Log.d("MainActivity", line);
                }

            }
        });

         */

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK);
                finish();
                //finishAffinity();
            }
        });

    }

    /*
    public String getBtnClique() {
        return btnClique;
    }
     */


    private String readCsvFromFileArrayList(String f1) {
        String data = "";
        try {
            //vérifier si le fichier existe, si non, renvoyer une chaîne vide
            String fileName = f1;
            File file = new File(privateDir, fileName);
            if (!file.exists()) {
                return data;
            }

            //Lire le contenu d'un fichier
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            line = reader.readLine(); // Ignore first line
            while ((line = reader.readLine()) != null) {
                try {
                    data += line;
                    data += "\n";
                } catch (NullPointerException e) {
                    //Ignore empty lines
                }
            }
            reader.close();

        } catch (IOException e) {
            Log.e("RaspActivity", "Error reading rasp.csv", e);
        }
        return data;
    }

    private void csvAfficher(String nameficher){
        // lire le contenu du fichier CSV rasp.csv
        String[] lines = readCsvFromFile(nameficher).split("\n");

        //afficher le contenu du fichier CSV rasp.csv ligne par ligne dans logcat
        for (String line : lines) {
            csv += line +"\n";
        }
        Log.d("MainActivity", csv);
    }

    private void showMsg(String msg){
        Toast.makeText(MainActivity.this, msg,Toast.LENGTH_LONG).show();
    }

    private String readCsvFromFile(String f1) {
        String data = "";
        try {

            //vérifier si le fichier existe, si non, renvoyer une chaîne vide
            String fileName = f1;
            File file = new File(privateDir, fileName);
            if (!file.exists()) {
                return data;
            }

            //Lire le contenu d'un fichier
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            //reader.readLine();
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) {
                    break;
                }
                data += line;
                data += "\n";
            }
            reader.close();

        } catch (IOException e) {
            Log.e("MainActivity", "Error reading rasp.csv", e);
        }
        return data;
    }

    //écrire dans un fichier CSV
    private void writeCsvToFile(String data) {
        try {
            //Vérifier si le fichier existe et le créer si ce n'est pas le cas
            String fileName = "rasp.csv";
            File file = new File(getExternalFilesDir(null), fileName);
            if (!file.exists()) {
                file.createNewFile();
            }

            else {
                // Vider le fichier
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                writer.write("");
                writer.close();
            }

            //Écrire des données dans le fichier
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
            writer.write(data);
            writer.newLine();
            writer.close();

        } catch (IOException e) {
            Log.e("MainActivity", "Error writing to rasp.csv", e);
        }
    }


    public void getIPandPort() {
        //String iPandPort = txtAddress.getText().toString();
        Log.d("MYTEST","IP String: "+ iPandPort);

        Pattern pattern = Pattern.compile("^([0-9]{1,3}.){3}[0-9]{1,3}:([0-9]{1,5})$");
        Matcher matcher = pattern.matcher(iPandPort);
        if (!matcher.matches()) {
            throw new RuntimeException("The hostname is invalid: " + iPandPort);
        }

        String temp[]= iPandPort.split(":");
        wifiModuleIp = temp[0];
        wifiModulePort = Integer.valueOf(temp[1]);
        Log.d("MY TEST","IP:" +wifiModuleIp);
        Log.d("MY TEST","PORT:"+wifiModulePort);
    }

    public class Socket_AsyncTask extends AsyncTask<Void, Void, String> {
        Socket socket;

        @Override
        protected String doInBackground(Void... params) {
            String response = "";
            try {
                InetAddress inetAddress = InetAddress.getByName(MainActivity.wifiModuleIp);
                socket = new Socket(inetAddress, MainActivity.wifiModulePort);

                //Envoyer une commande
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataOutputStream.writeBytes(CMD);
                dataOutputStream.flush();  // Rafraîchir le flux de sortie pour s'assurer que les données sont envoyées au serveur
                Log.d("MyApp", "CMD sent: " + CMD);

                // Recevoir la réponse d'un Raspberry Pi
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                response = bufferedReader.readLine();

            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (socket != null) {
                        socket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return response;
        }

        /*
        @Override
        protected void onPostExecute(String result) {
            // Traiter la réponse d'un Raspberry Pi
            // Mettre à jour l'interface utilisateur ici, par exemple mettre à jour le contenu d'un TextView
            if ("lire".equals(CMD)) {
                // Si le bouton btn4 est pressé et que le Raspberry Pi renvoie "Message from Raspberry Pi: Command received", alors définissez le texte de t1
                t1.setText(result);
            }
        }
         */
    }


}
