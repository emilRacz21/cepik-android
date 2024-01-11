package com.example.bar;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setActionBar("  Strona główna", R.drawable.baseline_home_24);
        // Change text color of the ActionBar title
        getSupportActionBar().setBackgroundDrawable(ContextCompat.getDrawable(this,R.color.colorPrimary));
        getSupportActionBar().setDisplayOptions(
                ActionBar.DISPLAY_SHOW_TITLE
                        | ActionBar.DISPLAY_SHOW_HOME
                        | ActionBar.DISPLAY_USE_LOGO
                        | ActionBar.DISPLAY_SHOW_CUSTOM);

        setFragment(new VoivodeshipFragment());
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NotNull MenuItem item) {

        if(item.getItemId() == R.id.settings){
            Toast.makeText(this,"Ustawienia",Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == android.R.id.home) {
            setActionBar("  Strona główna", R.drawable.baseline_home_24);
            setFragment(new VoivodeshipFragment());
        } else {
            return super.onOptionsItemSelected(item);
        }
        return false;
    }

    void setFragment(Fragment fragmenty){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment, fragmenty);
        transaction.commit();
    }

    void setActionBar(String title, int logo){
        SpannableString spannableString = new SpannableString(title);
        spannableString.setSpan(new RelativeSizeSpan(1.2f), 0, spannableString.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        getSupportActionBar().setTitle(spannableString);
        getSupportActionBar().setLogo(logo);
    }
}