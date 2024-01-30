package com.example.unlock3;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText username, password, hostname;
    private Button update, cancel;

    private MyDBhelper myDBhelper;

    private int userId;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        init();
    }

    private void init(){
        this.username = findViewById(R.id.username);
        this.password = findViewById(R.id.password);
        this.hostname = findViewById(R.id.hostname);

        this.update = findViewById(R.id.update);
        this.update.setOnClickListener(this);
        this.cancel = findViewById(R.id.cancel);
        this.cancel.setOnClickListener(this);

        Intent intent = getIntent();
        this.userId = intent.getIntExtra("userId", -1);

        //trouver l'historique selon username
        this.myDBhelper = new MyDBhelper(UserActivity.this, "note.db", null, 1);
        this.user = myDBhelper.queryUserbyId(userId);

        this.username.setText(this.user.getUserName());
        this.password.setText(this.user.getPassword());
        this.hostname.setText(this.user.getHostname());

    }

    private void validation(User user1) {

        if(user1.getPassword().length()<1 || user1.getHostname().length()<1){
            throw new RuntimeException("Tous les champs sont obligatoires.");
        }
        //password
        if (user1.getPassword().length() < 6 || user1.getPassword().length() > 10) {
            throw new RuntimeException("La longueur du mot de passe doit être comprise entre 6 et 10 caractères.");
        }

        //hostname
        if (user1.getHostname().length() >= 10 && user1.getHostname().length() <= 21) {
            Pattern pattern = Pattern.compile("^([0-9]{1,3}.){3}[0-9]{1,3}:([0-9]{1,5})$");
            Matcher matcher = pattern.matcher(user1.getHostname());

            if (!matcher.matches()) {
                throw new RuntimeException("The hostname is invalid: " + user1.getHostname());
            }

            boolean findHostname = myDBhelper.isHostnameExists(this.userId, user1.getHostname());
            if (findHostname) {
                throw new RuntimeException("Le hostname est déjà utilisé.");
            }
        } else {
            throw new RuntimeException("La longueur du hostname doit être comprise entre 10 et 21 caractères.");
        }


    }



    @Override
    public void onClick(View view) {

        String getpassword = this.password.getText().toString();
        String gethostname = this.hostname.getText().toString();
        User user1=new User();
        user1.setPassword(getpassword);
        user1.setHostname(gethostname);

        if(view == this.update){
            try{
                validation(user1);
                boolean flag = myDBhelper.updateUser(this.userId, user1.getPassword(), user1.getHostname());
                if(flag){
                    showMsg("Modification réussie.");
                    setResult(RESULT_CANCELED);
                    finish();
                }else{
                    showMsg("Modification échouée.");
                }
            }
            catch (RuntimeException e){
                showMsg(e.getMessage());
            }
            catch (Exception e){
                showMsg(e.getMessage());
            }
        }
        else if(view == this.cancel){
            setResult(RESULT_CANCELED);
            finish();
        }
    }
    private void showMsg(String msg){
        Toast.makeText(UserActivity.this, msg,Toast.LENGTH_LONG).show();
    }
}