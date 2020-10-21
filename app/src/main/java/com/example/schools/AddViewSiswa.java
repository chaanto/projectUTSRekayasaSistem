package com.example.schools;

import android.annotation.SuppressLint;
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

public class AddViewSiswa extends AppCompatActivity implements View.OnClickListener{
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


    private Button buttonAddRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_view_siswa);

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

        buttonAddRecord = (Button) findViewById(R.id.buttonAddRecord);

        buttonAddRecord.setOnClickListener(this);

        editDob.setInputType(InputType.TYPE_NULL);
        editDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(AddViewSiswa.this,
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


        new GetPosisiFromServer().execute();
    }

    private void addSiswa(){

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

        class AddEmployee extends AsyncTask<Void,Void,String>{

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(AddViewSiswa.this,"Adding...","Watting...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(AddViewSiswa.this,s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                params.put(konfigurasi.KEY_NO_INDUK,no_induk);
                params.put(konfigurasi.KEY_NAMA_SISWA,nama_siswa);
                params.put(konfigurasi.KEY_SEX,sex);
                params.put(konfigurasi.KEY_DOB,dob);
                params.put(konfigurasi.KEY_DOB_PLACE,dob_place);
                params.put(konfigurasi.KEY_ALAMAT,alamat);
                params.put(konfigurasi.KEY_WALI,wali);
                params.put(konfigurasi.KEY_WALI_NO,wali_no);
                params.put(konfigurasi.KEY_ID_SEKOLAH,id_sekolah);
                params.put(konfigurasi.KEY_ID_PAKET,id_paket);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(konfigurasi.URL_ADD_SISWA, params);
                return res;
            }
        }

        AddEmployee ae = new AddEmployee();
        ae.execute();
    }

    @Override
    public void onClick(View view) {
        if (view == buttonAddRecord) {
            if (editTextSiswa.getText().toString().length() == 0)
                editTextSiswa.setError("Nama Siswa wajib di isi!");

            if (editTextNoinduk.getText().toString().length() == 0)
                editTextNoinduk.setError("No Induk wajib di isi!");

            addSiswa();
            startActivity(new Intent(AddViewSiswa.this,ListViewSiswa.class));
        }
    }

    private class GetPosisiFromServer extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AddViewSiswa.this);
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

    }

}


