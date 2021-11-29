package sih.cvrce.neper_farmer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import hotchemi.android.rate.AppRate;
import sih.cvrce.neper_farmer.utility.Tools;

public class HomeScreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private Tools tools;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        tools = new Tools(this);

        setClickListeners();
    }

    private void setClickListeners(){
        findViewById(R.id.card_cabbage).setOnClickListener(this);
        findViewById(R.id.card_maize).setOnClickListener(this);
        findViewById(R.id.card_cotton).setOnClickListener(this);
        findViewById(R.id.card_rice).setOnClickListener(this);
        findViewById(R.id.card_potato).setOnClickListener(this);
        findViewById(R.id.card_wheat).setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.lang_en) {
            tools.savePref(Tools.PREF_LANG, Tools.LANG_EN);
            tools.setLocale(Tools.LANG_EN);
            refreshPage();
            return true;
        }

        if(id==R.id.lang_hi){
            tools.savePref(Tools.PREF_LANG, Tools.LANG_HI);
            tools.setLocale(Tools.LANG_HI);
            refreshPage();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void refreshPage(){
        Intent intent = new Intent(this, HomeScreen.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_about) {
            // Handle the camera action
            Intent about = new Intent(HomeScreen.this, AboutUs.class);
            startActivity(about);
        } else if (id == R.id.nav_rating) {
            AppRate.with(this).showRateDialog(this);
        } else if (id == R.id.nav_share) {
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
                String shareMessage= "\nLet me recommend you this application\n\n";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id="
                        + BuildConfig.APPLICATION_ID +"\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "Choose one"));
            } catch(Exception e) {
                //e.toString();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(HomeScreen.this, ImageUpload.class);
        switch (v.getId()){
            case R.id.card_cabbage:
                intent.putExtra(Tools.INTENT_EXTRA_CROP_TYPE, "cabbage");
                startActivity(intent);
                break;
            case R.id.card_maize:
                intent.putExtra(Tools.INTENT_EXTRA_CROP_TYPE, "maze");
                startActivity(intent);
                break;
            case R.id.card_cotton:
                intent.putExtra(Tools.INTENT_EXTRA_CROP_TYPE, "cotton");
                startActivity(intent);
                break;
            case R.id.card_rice:
                intent.putExtra(Tools.INTENT_EXTRA_CROP_TYPE, "rice");
                startActivity(intent);
                break;
            case R.id.card_potato:
                intent.putExtra(Tools.INTENT_EXTRA_CROP_TYPE, "potato");
                startActivity(intent);
                break;
            case R.id.card_wheat:
                intent.putExtra(Tools.INTENT_EXTRA_CROP_TYPE, "wheat");
                startActivity(intent);
                break;
        }
    }
}