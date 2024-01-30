package com.example.unlock3;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisteActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText username, password, hostname;
    private Button registe, cancel;

    private MyDBhelper myDBhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registe);
        init();

    }

    private void init(){
        this.username = findViewById(R.id.username);
        this.password = findViewById(R.id.password);
        this.hostname = findViewById(R.id.hostname);
        this.registe = findViewById(R.id.registe);
        this.cancel = findViewById(R.id.cancel);

        this.registe.setOnClickListener(this);
        this.cancel.setOnClickListener(this);

        this.myDBhelper = new MyDBhelper(RegisteActivity.this, "note.db", null, 1);
    }

    private void validation(User user) {

        if(user.getUserName().length()<1 || user.getPassword().length()<1 || user.getHostname().length()<1){
            throw new RuntimeException("Tous les champs sont obligatoires.");
        }
        //username
        if (user.getUserName().length() >= 4 && user.getUserName().length() <= 10) {
            boolean findUser = this.myDBhelper.isUsernameExists(user.getUserName());

            if (findUser) {
                throw new RuntimeException("Le nom d'utilisateur est déjà utilisé.");
            }
        } else {
            throw new RuntimeException("La longueur du nom d'utilisateur doit être comprise entre 4 et 10 caractères.");
        }

        //password
        if (user.getPassword().length() < 6 || user.getPassword().length() > 10) {
            throw new RuntimeException("La longueur du mot de passe doit être comprise entre 6 et 10 caractères.");
        }

        //hostname
        if (user.getHostname().length() >= 10 && user.getHostname().length() <= 21) {
            Pattern pattern = Pattern.compile("^([0-9]{1,3}.){3}[0-9]{1,3}:([0-9]{1,5})$");
            Matcher matcher = pattern.matcher(user.getHostname());

            if (!matcher.matches()) {
                throw new RuntimeException("The hostname is invalid: " + user.getHostname());
            }

            boolean findHostname = myDBhelper.isHostnameExists(-1, user.getHostname());
            if (findHostname) {
                throw new RuntimeException("Le hostname est déjà utilisé.");
            }
        } else {
            throw new RuntimeException("La longueur du hostname doit être comprise entre 10 et 21 caractères.");
        }


    }

    @Override
    public void onClick(View view) {
        String getusername = username.getText().toString();
        String getpassword = password.getText().toString();
        String gethostname = hostname.getText().toString();

        User user=new User();
        user.setUserName(getusername);
        user.setPassword(getpassword);
        user.setHostname(gethostname);

        if(view == this.registe){
            try{
                validation(user);
                boolean flag = myDBhelper.insertUser(user.getUserName(), user.getPassword(), user.getHostname());
                if(flag){
                    showMsg("Inscription réussie.");
                    finish();
                }else{
                    showMsg("Échec de l'inscription.");
                }
            }
            catch (RuntimeException e){
                showMsg(e.getMessage());
            }
            catch (Exception e){
                showMsg(e.getMessage());
            }
        }
        else if (view == this.cancel) {
            finish();
        }
    }

    private void showMsg(String msg){
        Toast.makeText(RegisteActivity.this, msg,Toast.LENGTH_LONG).show();
    }
}