package com.example.bar;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.Menu;
import android.view.MenuItem;
import org.jetbrains.annotations.NotNull;
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setActionBar("  Formularz", R.drawable.baseline_assignment_24);
        getSupportActionBar().setBackgroundDrawable(ContextCompat.getDrawable(this,R.color.colorPrimary));
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_USE_LOGO | ActionBar.DISPLAY_SHOW_CUSTOM);
        setFragment(new FormFragment());
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setActionBar("  Formularz", R.drawable.baseline_assignment_24);
            setFragment(new FormFragment());
        }if(item.getItemId() == R.id.formFull){
            setActionBar("  Formularz", R.drawable.baseline_assignment_24);
            setFragment(new FormFragment());
        }if(item.getItemId() == R.id.idFrom){
            setActionBar("  Formularz po ID", R.drawable.baseline_assignment_24);
            setFragment(new IdCarFragment());
        }
        else {
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