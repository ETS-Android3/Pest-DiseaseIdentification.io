package sih.cvrce.neper_expert;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import sih.cvrce.neper_expert.utility.Tools;

public class MainActivity extends AppCompatActivity {

    private Tools tools;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tools = new Tools(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(tools.isLoggedIn()){
                    Intent intent = new Intent(MainActivity.this, ExpertHome.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(MainActivity.this, Login.class);
                    startActivity(intent);
                }
                finish();
            }
        }, 800);
    }
}