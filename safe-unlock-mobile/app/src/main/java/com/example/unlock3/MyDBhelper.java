package com.example.unlock3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyDBhelper extends SQLiteOpenHelper {
    private SQLiteDatabase db;

    //créer bbd et tableau
    public MyDBhelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table user(id integer primary key autoincrement, username varchar(10), password varchar(10), hostname text)");
        sqLiteDatabase.execSQL("create table note(id integer primary key autoincrement, content text, note_time text, user_id integer)");
        //sqLiteDatabase.execSQL("create table user_note(user_id integer, note_id integer, primary key(user_id, note_id))");
    }

    //le tableau note
    //ajouter des données
    public boolean insertNote(String content, int userId) {
        // Générer l'heure actuelle
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String finalDate = sdf.format(date);

        // Insérer des données
        ContentValues contentValues = new ContentValues();
        contentValues.put("content", content);
        contentValues.put("note_time", finalDate);
        contentValues.put("user_id", userId);

        long i = db.insert("note", null, contentValues);

        //return (int)i;
        return i > 0 ? true : false;
    }




    //insert le tableau user
    public boolean insertUser(String username, String password, String hostname){
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("hostname", hostname);

        long i = db.insert("user", null, contentValues);

        return i > 0 ? true : false;
    }


    //modifier le tableau user
    //pas besoin modifier id, pas username, donc on a besoin password
    //on a besoin d'indice username
    public boolean updateUser(int userId, String password, String hostname) {
        // Vérifier la validité des paramètres
        if (userId <= 0 || password == null || hostname == null) {
            return false;
        }

        // Construire la condition de mise à jour
        String whereClause = "id=?";
        String[] whereArgs = new String[]{String.valueOf(userId)};

        // Construire le contenu de la mise à jour
        ContentValues contentValues = new ContentValues();
        contentValues.put("password", password);
        contentValues.put("hostname", hostname);

        // Exécuter l'opération de mise à jour
        int rowsAffected = this.db.update("user", contentValues, whereClause, whereArgs);

        // Retourner le résultat de la mise à jour
        return rowsAffected > 0;
    }

    /*
    public boolean updateUser(int userId, String password, String hostname){
        ContentValues contentValues = new ContentValues();
        contentValues.put("password", password);
        contentValues.put("hostname", hostname);

        long i = this.db.update("user", contentValues, "id=?", new String[]{password, hostname});

        return i > 0 ? true : false;
    }

     */

    //supprimer selon id (pas besoin?) user
    public boolean deleteUser(String deleteId){
        int i = this.db.delete("user", "id=?", new String[]{deleteId});
        return i > 0 ? true : false;
    }

    //request user, donc juste une ligne
    public User queryUserbyUsername(String username){
        Cursor cursor = this.db.query("user", null, "username=?", new String[]{username}, null, null,null,null);
        if(cursor.getCount()==1){
            cursor.moveToFirst();
            int id = cursor.getInt(0);
            String username1 = cursor.getString(1);
            String password = cursor.getString(2);
            String hostname = cursor.getString(3);
            //il y a d'autre méthode
            User user = new User();
            user.setId(id);
            user.setUserName(username1);
            user.setPassword(password);
            user.setHostname(hostname);
            cursor.close();

            return user;
        }
        else{
            return null;
        }
    }

    public boolean isUsernameExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT * FROM user WHERE username = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{username});

        boolean exists = cursor.getCount() > 0;
        cursor.close();

        return exists;
    }


    public boolean isHostnameExists(int userId, String hostname) {
        SQLiteDatabase db = this.getReadableDatabase();

        boolean exists = false;

        if (userId == -1) {
            String sql = "SELECT * FROM user WHERE hostname = ?";
            Cursor cursor = db.rawQuery(sql, new String[]{hostname});

            if (cursor.getCount() > 0) {
                exists = true;
            }

            cursor.close();
        } else {
            String sql = "SELECT * FROM user WHERE hostname = ? AND id != ?";
            Cursor cursor = db.rawQuery(sql, new String[]{hostname, Integer.toString(userId)});

            if (cursor.getCount() > 0) {
                exists = true;
            }

            cursor.close();
        }

        return exists;
    }


    /*
    public boolean isHostnameExists(int userId, String hostname) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = null;

        if(userId == -1){
            sql = "SELECT * FROM user WHERE hostname = ?";
        }
        else{
            sql = "SELECT * FROM user WHERE hostname = ? AND userId != ?";
        }

        Cursor cursor = db.rawQuery(sql, new String[]{Integer.toString(userId), hostname});

        boolean exists = cursor.getCount() > 0;
        cursor.close();

        return exists;
    }

     */



    public User queryUserbyUsernamePassword(String username, String password){
        Cursor cursor = this.db.query("user", null, "username=? AND password=?", new String[]{username, password}, null, null,null,null);
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            int id = cursor.getInt(0);
            String username1 = cursor.getString(1);
            String password1 = cursor.getString(2);
            String hostname = cursor.getString(3);
            User user = new User();
            user.setId(id);
            user.setUserName(username1);
            user.setPassword(password1);
            user.setHostname(hostname);
            cursor.close();
            return user;
        } else {
            return null;
        }
    }


    public User queryUserbyId(int userId){
        Cursor cursor = this.db.query("user", null, "id=?", new String[]{String.valueOf(userId)}, null, null,null,null);
        if(cursor.getCount()==1){
            cursor.moveToFirst();
            int id = cursor.getInt(0);
            String username1 = cursor.getString(1);
            String password = cursor.getString(2);
            String hostname = cursor.getString(3);
            //il y a d'autre méthode
            User user = new User();
            user.setId(id);
            user.setUserName(username1);
            user.setPassword(password);
            user.setHostname(hostname);
            cursor.close();

            return user;
        }
        else{
            return null;
        }
    }


    //p28
    //supprimer selon id (pas besoin?) note
    public boolean deleteData(String deleteId){
        int i = this.db.delete("note", "id=?", new String[]{deleteId});
        return i > 0 ? true : false;
    }

    //request tous les données(sans condition), utiliser attributs d'instance Note pour stocker les données
    //et stocker dans la collection ArrayList
    public ArrayList<Note> query(){
        ArrayList<Note> list = new ArrayList<>();
        Cursor cursor = db.query("note", null, null, null,null, null, null, null);
        while(cursor.moveToNext()){
            Note notePad = new Note();
            notePad.setId(Integer.valueOf(cursor.getInt(0)));
            notePad.setContent(cursor.getString(1));
            notePad.setNote_time(cursor.getString(2));
            list.add(notePad);
            //cursor.close();
        }
        cursor.close();
        return list;
    }

    public ArrayList<Note> queryNoteByUser(int userId) {
        // Créer une requête
        String query = "SELECT * FROM note WHERE user_id = ?";

        // Exécuter la requête
        Cursor cursor = this.getReadableDatabase().rawQuery(query, new String[]{String.valueOf(userId)});

        // Créer une liste de notes
        ArrayList<Note> notes = new ArrayList<>();

        // Parcourir les résultats de la requête
        while (cursor.moveToNext()) {
            // Créer un objet note
            Note note = new Note();

            // Définir les propriétés de la note
            note.setId(Integer.valueOf(cursor.getInt(0)));
            note.setContent(cursor.getString(1));
            note.setNote_time(cursor.getString(2));
            note.setUser_id(cursor.getInt(cursor.getInt(3)));

            // Ajouter la note à la liste
            notes.add(note);
        }

        // Fermer le curseur
        cursor.close();

        // Retourner la liste de notes
        return notes;
    }



    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
