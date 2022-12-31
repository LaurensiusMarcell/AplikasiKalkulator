package com.example.aplikasikalkulator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;


public class MainActivity extends AppCompatActivity {


    Spinner iniSpiner;
    RecyclerView histori;
    Gson gerson;
    SharedPreferences pref;
    EditText Input1, Input2;
    TextView operator, outputHasil;
    ArrayList<item> HistoryList, ListInit;
    String jsonStr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        pref = this.getSharedPreferences(getString(R.string.shared_pref), Context.MODE_PRIVATE);
        gerson = new GsonBuilder().create();

        operator = findViewById(R.id.tandaOperator);
        outputHasil = findViewById(R.id.iniHasil);

        Input1 = findViewById(R.id.inputAngka1);
        Input2 = findViewById(R.id.inputAngka2);

        iniSpiner = findViewById(R.id.spinnerOperasi);
        iniSpiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String temp = (String) adapterView.getItemAtPosition(i);
                if (temp == "(+) Tambah"){
                    operator.setText("+");
                }else if(temp == "(-) Kurang"){
                    operator.setText("-");
                }else if(temp == "(x) Kali"){
                    operator.setText("x");
                }else{
                    operator.setText(":");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                operator.setText("");
            }
        });

        ArrayList <String> listOperasi = new ArrayList<>();
        listOperasi.add("(+) Tambah");
        listOperasi.add("(-) Kurang");
        listOperasi.add("(x) Kali");
        listOperasi.add("(:) Bagi");

        ArrayAdapter iniAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listOperasi);
        iniSpiner.setAdapter(iniAdapter);

        // nggawe list seko shared prefrences seko Json
        jsonStr = pref.getString(getString(R.string.data_simpanan), "[]");
        ListInit = gerson.fromJson(jsonStr, new TypeToken<ArrayList<item>>(){}.getType());
        if (ListInit == null) ListInit = new ArrayList<item>(); // ben nek misal kosong ora error


        histori = findViewById(R.id.histori);
        histori.setAdapter(new AdapterRecycleView(ListInit, this));
        histori.setLayoutManager(new LinearLayoutManager(this));





    }

    public void tombolEnter(View v){
        int Angka1, Angka2;
     try {
         Angka1 = Integer.parseInt(Input1.getText().toString());
         Angka2 = Integer.parseInt(Input2.getText().toString());
     }catch (Exception e){
         Toast.makeText(this,"Pastikan input yang dimasukkan benar !", Toast.LENGTH_SHORT).show();

         return;

     }
        String pilihan = (String) iniSpiner.getSelectedItem();

        int hasil;
        String op;
        if (pilihan == "(+) Tambah"){
            hasil = Angka1 + Angka2;
            op = "+";
            outputHasil.setText(Integer.toString(hasil));
        }else if(pilihan == "(-) Kurang"){
            hasil = Angka1 - Angka2;
            op = "-";
            outputHasil.setText(Integer.toString(hasil));
        }else if(pilihan == "(x) Kali"){
            hasil = Angka1*Angka2;
            op = "x";
            outputHasil.setText(Integer.toString(hasil));
        }else{
            hasil = Angka1/Angka2;
            op = ":";
            outputHasil.setText(Integer.toString(hasil));
        }

        String temp2 = pref.getString(getString(R.string.data_simpanan), "[]");
        HistoryList = gerson.fromJson(temp2, new TypeToken<ArrayList<item>>(){}.getType());
        if (temp2 == null) HistoryList = new ArrayList<item>(); // iki shorthand syntax, menawane jebule kosong isine ben ora error

        HistoryList.add(new item(Angka1, Angka2, op, hasil));

        // string sing mau di enggo meneh, ben ora boros :v
        temp2 = gerson.toJson(HistoryList);

        // saiki dilebokke ng string mau nggowo share prefrences
        pref.edit().putString(getString(R.string.data_simpanan), temp2).apply();

        // diubah tampilane sisan pas di klik
        histori = findViewById(R.id.histori);
        histori.setAdapter(new AdapterRecycleView(HistoryList, this));
        histori.setLayoutManager(new LinearLayoutManager(this));

        // menyembunyikan keyboard
        hideKeyboard(this);

    }


    private static void hideKeyboard(Activity activity) {  // kode dari github buat menyembunyikan keyboard
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }



}


