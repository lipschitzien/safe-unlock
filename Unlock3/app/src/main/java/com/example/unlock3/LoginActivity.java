package com.example.unlock3;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnRegister, btnLogin;
    private EditText username, password;

    private MyDBhelper myDBhelper;

    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }
    private void init(){
        this.username = findViewById(R.id.username);
        this.password = findViewById(R.id.password);

        this.btnLogin = findViewById(R.id.btnLogin);
        this.btnLogin.setOnClickListener(this);

        this.btnRegister = findViewById(R.id.btnRegister);
        this.btnRegister.setOnClickListener(this);

        this.userId = -1;

    }

    @Override
    public void onClick(View view) {
        if(view == this.btnRegister){
            Intent intent = new Intent(LoginActivity.this, RegisteActivity.class);
            startActivity(intent);
        }
        else if(view == this.btnLogin){
            String getUsername = this.username.getText().toString();
            String getPassword = this.password.getText().toString();
            //chercher dans le tableau user selon les champs que les utilisateurs ont écrit
            this.myDBhelper = new MyDBhelper(LoginActivity.this, "note.db", null,1);
            User user = myDBhelper.queryUserbyUsernamePassword(getUsername, getPassword);


            if(user == null){
                Toast.makeText(LoginActivity.this, "Votre nom d'utilisateur ou votre mot de passe est incorrect.", Toast.LENGTH_LONG).show();
            }

            //vérifier le mot de passe
            //else if(user.getPassword().equals(getPassword)){
            else{
                this.userId = user.getId();

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("userId", userId);
                //startActivity(intent);
                startActivityForResult(intent, 1);

            }

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            //Passer à MainActivity avec succès
            this.password.setText("");
        }
    }
}