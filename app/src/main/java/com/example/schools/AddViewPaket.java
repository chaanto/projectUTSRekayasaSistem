package com.example.schools;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddViewPaket extends AppCompatActivity implements View.OnClickListener{
    private EditText editTextPaket;
    private Button buttonAddRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_view_paket);

        editTextPaket = (EditText) findViewById(R.id.editTextPaket);

        buttonAddRecord = (Button) findViewById(R.id.buttonAddRecord);

        buttonAddRecord.setOnClickListener(this);
    }

    private void addPaket(){

        final String nama_paket = editTextPaket.getText().toString().trim();

        class AddPaket extends AsyncTask<Void,Void,String>{

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(AddViewPaket.this,"Menambahkan...","Tunggu...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(AddViewPaket.this,s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                params.put(konfigurasi.KEY_NAMA_PAKET,nama_paket);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(konfigurasi.URL_ADD_PAKET, params);
                return res;
            }
        }

        AddPaket ae = new AddPaket();
        ae.execute();
    }

    @Override
    public void onClick(View view) {
        if(view == buttonAddRecord){
            addPaket();
            startActivity(new Intent(AddViewPaket.this,ListViewPaket.class));
        }

    }

    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}
