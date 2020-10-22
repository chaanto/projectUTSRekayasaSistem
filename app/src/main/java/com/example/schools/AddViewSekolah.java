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

public class AddViewSekolah extends AppCompatActivity implements View.OnClickListener{
    private EditText editTextSekolah;
    private Button buttonAddRecord;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_view_sekolah);

        editTextSekolah = (EditText) findViewById(R.id.editTextSekolah);

        buttonAddRecord = (Button) findViewById(R.id.buttonAddRecord);

        buttonAddRecord.setOnClickListener(this);
    }

    private void addSekolah(){

        final String nama_sekolah = editTextSekolah.getText().toString().trim();

        class AddSekolah extends AsyncTask<Void,Void,String>{

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(AddViewSekolah.this,"Adding...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(AddViewSekolah.this,s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                params.put(konfigurasi.KEY_NAMA_SEKOLAH,nama_sekolah);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(konfigurasi.URL_ADD_SEKOLAH, params);
                return res;
            }
        }

        AddSekolah ae = new AddSekolah();
        ae.execute();
    }

    @Override
    public void onClick(View view) {
        if(view == buttonAddRecord){
            addSekolah();
            startActivity(new Intent(AddViewSekolah.this,ListViewSekolah.class));
        }

    }

}
