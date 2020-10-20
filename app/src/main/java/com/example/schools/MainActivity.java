package com.example.schools;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonViewPaket;
    private Button buttonViewSekolah;
    private Button buttonViewSiswa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonViewPaket = (Button) findViewById(R.id.buttonViewPaket);
        buttonViewSekolah = (Button) findViewById(R.id.buttonViewSekolah);
        buttonViewSiswa = (Button) findViewById(R.id.buttonViewSiswa);

        buttonViewPaket.setOnClickListener( this);
        buttonViewSekolah.setOnClickListener( this);
        buttonViewSiswa.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if(view == buttonViewPaket){
            startActivity(new Intent(this,ListViewPaket.class));
        }
        if(view == buttonViewSekolah){
            startActivity(new Intent(this,ListViewSekolah.class));
        }
        if(view == buttonViewSiswa){
            startActivity(new Intent(this,ListViewSiswa.class));
        }
    }
}