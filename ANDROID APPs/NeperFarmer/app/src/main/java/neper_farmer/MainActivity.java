package sih.cvrce.neper_farmer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import sih.cvrce.neper_farmer.utility.Tools;

public class MainActivity extends AppCompatActivity {

    private Tools tools;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tools = new Tools(this);

        if(tools.getPrefValue(Tools.PREF_LANG, Tools.LANG_EN).equalsIgnoreCase(Tools.LANG_HI)){
            tools.savePref(Tools.PREF_LANG, Tools.LANG_HI);
            tools.setLocale(Tools.LANG_HI);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(tools.isLoggedIn()){
                    Intent intent = new Intent(MainActivity.this, HomeScreen.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(MainActivity.this, LanguageChooser.class);
                    startActivity(intent);
                }
                finish();
            }
        }, 1000);
    }
}
