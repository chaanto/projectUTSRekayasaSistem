package com.example.schools;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ShowSekolah extends AppCompatActivity implements View.OnClickListener{
    private EditText editTextSekolah;

    private Button buttonUpdate;
    private Button buttonDelete;

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_sekolah);

        Intent intent = getIntent();

        id = intent.getStringExtra(konfigurasi.SEKOLAH_ID);

        editTextSekolah = (EditText) findViewById(R.id.editTextSekolah);

        buttonUpdate = (Button) findViewById(R.id.buttonUpdate);
        buttonDelete = (Button) findViewById(R.id.buttonDelete);

        buttonUpdate.setOnClickListener(this);
        buttonDelete.setOnClickListener(this);

        getSekolah();
    }

    private void getSekolah(){
        class GetSekolah extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ShowSekolah.this,"Fetching...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showSekolah(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(konfigurasi.URL_GET_SEKOLAH,id);
                return s;
            }
        }
        GetSekolah ge = new GetSekolah();
        ge.execute();
    }

    private void showSekolah(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(konfigurasi.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);
            String nama_sekolah = c.getString(konfigurasi.TAG_NAMA_SEKOLAH);

            editTextSekolah.setText(nama_sekolah);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void updateSekolah(){
        final String name = editTextSekolah.getText().toString().trim();

        class UpdateSekolah extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ShowSekolah.this,"Updating...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(ShowSekolah.this,s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put(konfigurasi.KEY_ID,id);
                hashMap.put(konfigurasi.KEY_NAMA_SEKOLAH,name);

                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(konfigurasi.URL_UPDATE_SEKOLAH,hashMap);

                return s;
            }
        }

        UpdateSekolah ue = new UpdateSekolah();
        ue.execute();
        startActivity(new Intent(ShowSekolah.this,ListViewSekolah.class));
    }

    private void deleteSekolah(){
        class DeleteSekolah extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ShowSekolah.this, "Updating...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(ShowSekolah.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(konfigurasi.URL_DELETE_SEKOLAH, id);
                return s;
            }
        }

        DeleteSekolah de = new DeleteSekolah();
        de.execute();
    }

    private void confirmDeleteSekolah(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Apakah Kamu Yakin Ingin Menghapus Sekolah ini?");

        alertDialogBuilder.setPositiveButton("Ya",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        deleteSekolah();
                        startActivity(new Intent(ShowSekolah.this,ListViewSekolah.class));
                    }
                });

        alertDialogBuilder.setNegativeButton("Tidak",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onClick(View v) {
        if(v == buttonUpdate){
            updateSekolah();
        }

        if(v == buttonDelete){
            confirmDeleteSekolah();
        }
    }


}
