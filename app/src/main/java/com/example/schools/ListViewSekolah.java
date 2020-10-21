package com.example.schools;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ListViewSekolah extends AppCompatActivity implements ListView.OnItemClickListener{
    private ListView listView;

    private String JSON_STRING;

    private Button buttonAddSekolah;

    @Override
    protected void onStart() {
        super.onStart();
        getJSON();

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_sekolah);
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);

        buttonAddSekolah = (Button) findViewById(R.id.buttonAddSekolah);

        buttonAddSekolah.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(view == buttonAddSekolah){
                    startActivity(new Intent(view.getContext(),AddViewSekolah.class));
                }
            }
        });
    }


    private void showPaket(){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(konfigurasi.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String id = jo.getString(konfigurasi.TAG_ID);
                String name = jo.getString(konfigurasi.TAG_NAMA_SEKOLAH);

                HashMap<String,String> paket = new HashMap<>();
                paket.put(konfigurasi.TAG_ID,id);
                paket.put(konfigurasi.TAG_NAMA_SEKOLAH,name);
                list.add(paket);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(
                ListViewSekolah.this, list, R.layout.list_item,
                new String[]{konfigurasi.TAG_ID,konfigurasi.TAG_NAMA_SEKOLAH},
                new int[]{R.id.id, R.id.name});

        listView.setAdapter(adapter);
    }

    private void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String>{

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ListViewSekolah.this,"Mengambil Data","Mohon Tunggu...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showPaket();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(konfigurasi.URL_GET_ALL_SEKOLAH);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, ShowSekolah.class);
        HashMap<String,String> map =(HashMap)parent.getItemAtPosition(position);
        String sekolahId = map.get(konfigurasi.TAG_ID).toString();
        intent.putExtra(konfigurasi.SEKOLAH_ID,sekolahId);
        startActivity(intent);
    }
}
