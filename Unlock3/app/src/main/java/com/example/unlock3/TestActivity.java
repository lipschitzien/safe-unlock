package com.example.unlock3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class TestActivity extends AppCompatActivity {

    private TextView test;

    private ArrayList<PersonRasp> resultList;
    private String ss;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        this.test = findViewById(R.id.test);

        ss = getIntent().getStringExtra("csv");
        //this.test.setText(ss);

        resultList = new ArrayList<>();


        // 使用；切割字符串
        String[] lines = ss.split(";");

        //Log.i("pp",String.valueOf(lines.length) );


        //this.test.setText(String.valueOf(lines.length)); //correct

        Log.i("11",String.valueOf(lines[1]) );
        //this.test.setText(lines[1]);

        // 从第二行开始，使用逗号切割
        for (int i = 1; i < lines.length-1; i++) {
            String[] items = lines[i].split(",");

            // 创建 PersonRasp 对象
            PersonRasp person = new PersonRasp();

            // 设置属性
            if (!items[0].equals("FIN")) {
                person.setId(Integer.parseInt(items[0]));
                person.setPersonname(items[1]);
                person.setResultat(items[2]);
                person.setDate(items[3]);

                // 添加到 ArrayList 中
                resultList.add(person);
            }
        }



        // 遍历 ArrayList
        for (PersonRasp person : resultList) {
            this.test.append(person.getId() + "," + person.getPersonname() + "," + person.getResultat() + "," + person.getDate() + "\n");
            // 打印属性
            //Log.i("==", person.getId() + "," + person.getPersonname() + "," + person.getResultat() + "," + person.getDate());
        }




    }


        /*
        if(this.resultList != null){
            this.resultList.clear();
        }

        this.resultList = new ArrayList<>();

        try {
            this.resultList= creerArrayList(csv);
            Log.i("==", "inininin");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Log.i("==", "ppp");
        System.out.println(this.resultList);
        Log.i("==", "oooooo");

         */
        //this.resultList = creerCsvList("rasp.csv");

        //this.test.setText(resultList.get(0).getPersonname());

    public ArrayList<PersonRasp> creerArrayList(String csv) throws IOException {
        // 创建一个 ArrayList 对象
        ArrayList<PersonRasp> resultList = new ArrayList<>();

        // 使用分号分割 CSV 字符串
        BufferedReader reader = new BufferedReader((new InputStreamReader(new FileInputStream(csv))));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                if (line.equals("FIN")) {
                    break;
                }

                // 跳过表头
                if (!line.startsWith("id")) {
                    // 将每一行转换为 PersonRasp 对象
                    PersonRasp person = new PersonRasp();
                    String[] items = line.split(",");
                    person.setId(Integer.parseInt(items[0]));
                    person.setDate(items[1]);
                    person.setResultat(items[2]);
                    person.setDate(items[3]);

                    // 将 PersonRasp 对象添加到 ArrayList 中
                    resultList.add(person);
                    Log.i("==", "h");
                    this.test.append(String.valueOf(resultList.size()));
                }
            }
        } catch (IOException e) {
            // 处理异常
            e.printStackTrace();
        }

        return resultList;
    }

    /*
    public ArrayList<PersonRasp> creerArrayList(String csv) throws IOException {
        // 创建一个 ArrayList 对象
        ArrayList<PersonRasp> resultList = new ArrayList<>();

        // 使用分号分割 CSV 字符串
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.equals("FIN")) {
                break;
            }

            // 跳过表头
            if (!line.startsWith("id")) {
                // 将每一行转换为 PersonRasp 对象
                PersonRasp person = new PersonRasp();
                String[] items = line.split(",");
                person.setId(Integer.parseInt(items[0]));
                person.setDate(items[1]);
                person.setResultat(items[2]);
                person.setDate(items[3]);

                // 将 PersonRasp 对象添加到 ArrayList 中
                resultList.add(person);
                this.test.append(String.valueOf(resultList.size()));
            }
        }

        return resultList;
    }
    */

    /*
    private ArrayList<PersonRasp> creerCsvList(String nameficher){
        //ArrayList<PersonRasp> resultList = new ArrayList<>();
        // lire le contenu du fichier CSV rasp.csv
        String[] lines = readCsvFromFile(nameficher).split("\n");

        //afficher le contenu du fichier CSV rasp.csv ligne par ligne dans logcat
        for (String line : lines) {
            String[] values = line.split(",");

            PersonRasp personRasp = new PersonRasp();
            personRasp.setId(Integer.parseInt(values[0]));
            personRasp.setPersonname(values[1]);
            personRasp.setResultat(values[2]);
            personRasp.setDate(values[3]);

            this.resultList.add(personRasp);
        }

        return this.resultList;
    }

     */

}