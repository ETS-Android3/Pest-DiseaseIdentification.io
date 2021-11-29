package sih.cvrce.neper_farmer;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

import sih.cvrce.neper_farmer.utility.Tools;

public class LanguageChooser extends AppCompatActivity {

    private Button btnEng, btnHindi;
    private Tools tools;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_chooser);

        btnEng = findViewById(R.id.btn_english);
        btnHindi = findViewById(R.id.btn_hindi);

        tools = new Tools(this);

        btnHindi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tools.savePref(Tools.PREF_LANG, Tools.LANG_HI);
                tools.setLocale(Tools.LANG_HI);
                Intent intent = new Intent(LanguageChooser.this, MobileReg.class);
                startActivity(intent);
                finish();
            }
        });

        btnEng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tools.savePref(Tools.PREF_LANG, Tools.LANG_EN);
                tools.setLocale(Tools.LANG_EN);
                Intent intent = new Intent(LanguageChooser.this, MobileReg.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
