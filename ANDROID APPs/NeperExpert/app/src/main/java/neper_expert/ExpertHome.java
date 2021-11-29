package sih.cvrce.neper_expert;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import sih.cvrce.neper_expert.utility.Tools;

public class ExpertHome extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert_home);

        setClickListeners();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(ExpertHome.this, UploadList.class);
        switch (v.getId()){
            case R.id.card_cabbage:
                intent.putExtra(Tools.INTENT_EXTRA_CROP_TYPE, "cabbage");
                startActivity(intent);
                break;
            case R.id.card_rice:
                intent.putExtra(Tools.INTENT_EXTRA_CROP_TYPE, "rice");
                startActivity(intent);
                break;
            case R.id.card_cotton:
                intent.putExtra(Tools.INTENT_EXTRA_CROP_TYPE, "cotton");
                startActivity(intent);
                break;
            case R.id.card_maize:
                intent.putExtra(Tools.INTENT_EXTRA_CROP_TYPE, "maize");
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

    private void setClickListeners(){
        findViewById(R.id.card_cabbage).setOnClickListener(this);
        findViewById(R.id.card_rice).setOnClickListener(this);
        findViewById(R.id.card_cotton).setOnClickListener(this);
        findViewById(R.id.card_maize).setOnClickListener(this);
        findViewById(R.id.card_potato).setOnClickListener(this);
        findViewById(R.id.card_wheat).setOnClickListener(this);
    }
}