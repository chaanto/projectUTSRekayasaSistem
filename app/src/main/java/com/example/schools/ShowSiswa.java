package com.example.schools;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import android.widget.DatePicker;
import android.app.DatePickerDialog;
import java.util.Calendar;
import android.text.InputType;
import android.widget.Spinner;


public class ShowSiswa extends AppCompatActivity implements View.OnClickListener{
    private EditText editTextSiswa;
    private EditText editTextNoinduk;
    private EditText editTextPlacedob;
    private EditText editTextAddress;
    private EditText editTextWali;
    private EditText editTextWalino;
    private EditText editDob;
    DatePickerDialog picker;

    private Spinner spinnerSekolah;
    private Spinner spinnerPaket;
    private Spinner spinnerSex;
    ArrayList<String> sekolahList = new ArrayList<>();
    ArrayList<String> sekolahidList = new ArrayList<>();
    ArrayList<String> paketList = new ArrayList<>();
    ArrayList<String> paketidList = new ArrayList<>();
    ProgressDialog pDialog;
    private String[] sexList ={"Laki-laki","Perempuan"};
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private String date_dob;

    private Button buttonUpdate;
    private Button buttonDelete;



    private String id;
    private String data_id_paket;
    private String data_id_sekolah;
    private String data_id_sex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_siswa);

        Intent intent = getIntent();

        id = intent.getStringExtra(konfigurasi.SISWA_ID);

        editTextSiswa = (EditText) findViewById(R.id.editTextSiswa);
        editTextNoinduk = (EditText) findViewById(R.id.editTextNoinduk);
        editTextPlacedob = (EditText) findViewById(R.id.editTextPlacedob);
        editDob = (EditText) findViewById(R.id.editDob);
        editTextAddress = (EditText) findViewById(R.id.editTextAddress);
        editTextWali = (EditText) findViewById(R.id.editTextWali);
        editTextWalino = (EditText) findViewById(R.id.editTextWalino);
        spinnerSekolah=(Spinner)findViewById(R.id.spinnerSekolah);
        spinnerPaket=(Spinner)findViewById(R.id.spinnerPaket);
        spinnerSex=(Spinner)findViewById(R.id.spinnerSex);
        final ArrayAdapter adapterSex = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,sexList);
        spinnerSex.setAdapter(adapterSex);


        editDob.setInputType(InputType.TYPE_NULL);


        editDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(ShowSiswa.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                editDob.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                date_dob =(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        buttonUpdate = (Button) findViewById(R.id.buttonUpdate);
        buttonDelete = (Button) findViewById(R.id.buttonDelete);

        buttonUpdate.setOnClickListener(this);
        buttonDelete.setOnClickListener(this);
        getSiswa();
        new ShowSiswa.GetPosisiFromServer().execute();
        spinnerSex.setSelection(adapterSex.getPosition(data_id_sex));
    }

    private void getSiswa(){
        class GetSiswa extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ShowSiswa.this,"Fetching...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showSiswa(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(konfigurasi.URL_GET_SISWA,id);
                return s;
            }
        }
        GetSiswa ge = new GetSiswa();
        ge.execute();
    }
    private void showSiswa(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(konfigurasi.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);
            String nama_siswa = c.getString(konfigurasi.TAG_NAMA_SISWA);
            String no_induk = c.getString(konfigurasi.TAG_NO_INDUK);
            String sex = c.getString(konfigurasi.TAG_SEX);
            String dob = c.getString(konfigurasi.TAG_DOB);
            String dob_place = c.getString(konfigurasi.TAG_DOB_PLACE);
            String alamat = c.getString(konfigurasi.TAG_ALAMAT);
            String wali = c.getString(konfigurasi.TAG_WALI);
            String wali_no = c.getString(konfigurasi.TAG_WALI_NO);
            String id_sekolah = c.getString(konfigurasi.TAG_ID_SEKOLAH);
            String id_paket = c.getString(konfigurasi.TAG_ID_PAKET);


//            spinnerSex.setSelection(2);
            if (dob != ""){
                String[] array = dob.split("-");
                dob = array[2] + "-"+array[1]+"-"+array[0];
                date_dob = dob;
            }


            editTextSiswa.setText(nama_siswa);
            editTextNoinduk.setText(no_induk);
            editDob.setText(dob);
            editTextPlacedob.setText(dob_place);
            editTextAddress.setText(alamat);
            editTextWali.setText(wali);
            editTextWalino.setText(wali_no);

            data_id_sex = sex;

            data_id_paket =id_paket;
            data_id_sekolah = id_sekolah;

//            spinnerSekolah.setSelection(Integer.valueOf(id_sekolah));
//            spinnerPaket.setSelection(Integer.valueOf(id_paket));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void updateSiswa(){
        final String nama_siswa = editTextSiswa.getText().toString().trim();
        final String no_induk = editTextNoinduk.getText().toString().trim();
        final String sex = spinnerSex.getSelectedItem().toString().trim();
        final String dob = date_dob.trim();
        final String dob_place = editTextPlacedob.getText().toString().trim();
        final String alamat = editTextAddress.getText().toString().trim();
        final String wali = editTextWali.getText().toString().trim();
        final String wali_no = editTextWalino.getText().toString().trim();
        final String id_paket = paketidList.get((int) spinnerPaket.getSelectedItemId()).trim();
        final String id_sekolah = paketidList.get((int) spinnerPaket.getSelectedItemId()).trim();


        class UpdateSiswa extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ShowSiswa.this,"Updating...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(ShowSiswa.this,s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put(konfigurasi.KEY_ID,id);
                hashMap.put(konfigurasi.KEY_NO_INDUK,no_induk);
                hashMap.put(konfigurasi.KEY_NAMA_SISWA,nama_siswa);
                hashMap.put(konfigurasi.KEY_SEX,sex);
                hashMap.put(konfigurasi.KEY_DOB,dob);
                hashMap.put(konfigurasi.KEY_DOB_PLACE,dob_place);
                hashMap.put(konfigurasi.KEY_ALAMAT,alamat);
                hashMap.put(konfigurasi.KEY_WALI,wali);
                hashMap.put(konfigurasi.KEY_WALI_NO,wali_no);
                hashMap.put(konfigurasi.KEY_ID_SEKOLAH,id_sekolah);
                hashMap.put(konfigurasi.KEY_ID_PAKET,id_paket);

                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(konfigurasi.URL_UPDATE_SISWA,hashMap);

                return s;
            }
        }

        UpdateSiswa ue = new UpdateSiswa();
        ue.execute();
//        startActivity(new Intent(ShowSiswa.this,ListViewSiswa.class));
        Intent intent = new Intent(ShowSiswa.this,ListViewSiswa.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void deleteSiswa(){
        class DeleteSiswa extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ShowSiswa.this, "Updating...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(ShowSiswa.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(konfigurasi.URL_DELETE_SISWA, id);
                return s;
            }
        }

        DeleteSiswa de = new DeleteSiswa();
        de.execute();
    }

    private void confirmDeleteSiswa(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Apakah Kamu Yakin Ingin Menghapus Siswa ini?");

        alertDialogBuilder.setPositiveButton("Ya",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        deleteSiswa();
//                        startActivity(new Intent(ShowSiswa.this,ListViewSiswa.class));
                        Intent intent = new Intent(ShowSiswa.this,ListViewSiswa.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
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
    public void onClick(View view) {
        if (view ==buttonUpdate){
            updateSiswa();
        }
        if(view == buttonDelete){
            confirmDeleteSiswa();
        }
    }



    private class GetPosisiFromServer extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ShowSiswa.this);
            pDialog.setMessage("Fetching Data");
            pDialog.show();


        }

        @Override
        protected Void doInBackground(Void... arg0) {
            Handler jsonParser = new Handler();
            String json_paket = jsonParser.makeServiceCall(konfigurasi.URL_GET_ALL_PAKET, Handler.GET);

            if (json_paket != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json_paket);
                    if (jsonObj != null) {
                        JSONArray model = jsonObj
                                .getJSONArray("result");

                        for (int i = 0; i < model.length(); i++) {
                            JSONObject modObj = (JSONObject) model.get(i);
                            String mod = new String(modObj.getString("id"));
                            String nama_paket = new String(modObj.getString("nama_paket"));
                            paketidList.add(mod);
                            paketList.add(nama_paket);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("JSON Data", "Didn't receive any data from server!");
            }

            String json_sekolah = jsonParser.makeServiceCall(konfigurasi.URL_GET_ALL_SEKOLAH, Handler.GET);

            if (json_sekolah != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json_sekolah);
                    if (jsonObj != null) {
                        JSONArray model = jsonObj
                                .getJSONArray("result");

                        for (int i = 0; i < model.length(); i++) {
                            JSONObject modObj = (JSONObject) model.get(i);
                            String mod = new String(modObj.getString("id"));
                            String nama_sekolah = new String(modObj.getString("nama_sekolah"));
                            sekolahidList.add(mod);
                            sekolahList.add(nama_sekolah);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("JSON Data", "Didn't receive any data from server!");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
            populateSpinnerModel();
        }
    }

    private void populateSpinnerModel() {
        List<String> lables_paket = new ArrayList<String>();
        List<String> lables_sekolah = new ArrayList<String>();

        for (int i = 0; i < paketList.size(); i++) {
            lables_paket.add(paketList.get(i));
        }
        for (int i = 0; i < sekolahList.size(); i++) {
            lables_sekolah.add(sekolahList.get(i));
        }

        ArrayAdapter<String> spinnerAdapterPaket = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables_paket);

        ArrayAdapter<String> spinnerAdaptersekolah = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables_sekolah);

        spinnerAdapterPaket.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAdaptersekolah.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerPaket.setAdapter(spinnerAdapterPaket);
        spinnerSekolah.setAdapter(spinnerAdaptersekolah);
        spinnerPaket.setSelection(spinnerAdapterPaket.getPosition(data_id_paket));

    }

}
