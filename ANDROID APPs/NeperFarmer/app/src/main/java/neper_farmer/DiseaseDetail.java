package sih.cvrce.neper_farmer;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sih.cvrce.neper_farmer.api.Api;
import sih.cvrce.neper_farmer.model.ApiDeseaseResponse;
import sih.cvrce.neper_farmer.model.ApiDeseaseResponseHi;
import sih.cvrce.neper_farmer.utility.Tools;

public class DiseaseDetail extends AppCompatActivity {

    private TextView txtName, txtCause, txtPrecaution, txtSymptoms, txtNatural_treatment, txtPesticide, txtCrop;
    private ImageView clear_image;

    private Tools tools;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_detail);

        txtName = findViewById(R.id.txt_dname);
//        txtCause = findViewById(R.id.txt_cause);
        txtPrecaution = findViewById(R.id.txt_precaution);
        txtSymptoms = findViewById(R.id.txt_symptom);
        txtNatural_treatment = findViewById(R.id.txt_natural_treatment);
        txtPesticide = findViewById(R.id.txt_pesticide);
        clear_image = findViewById(R.id.imageView2);

        int dId = getIntent().getIntExtra(Tools.INTENT_EXTRA_DISEASE_ID, 1);

        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Api api = retrofit.create(Api.class);
        tools = new Tools(this);

        if(tools.getPrefValue(Tools.PREF_LANG, Tools.LANG_EN).equalsIgnoreCase(Tools.LANG_HI)){
            getDiseaseDetailHi(api, dId);
        }else{
            getDiseaseDetailEn(api, dId);
        }

    }

    private void getDiseaseDetailEn(Api api, int dId){
        Call<List<ApiDeseaseResponse>> call = api.diseaseInfo(dId, tools.getPrefValue(Tools.PREF_LANG, Tools.LANG_EN));
        call.enqueue(new Callback<List<ApiDeseaseResponse>>() {
            @Override
            public void onResponse(Call<List<ApiDeseaseResponse>> call, Response<List<ApiDeseaseResponse>> response) {
                List<ApiDeseaseResponse> rs = response.body();
                if(rs.size()!=0) {
                    ApiDeseaseResponse adr = rs.get(0);
                    txtName.setText(getResources().getString(R.string.dd_txt_disease_name) + ": " + adr.name);
//                    txtCause.setText(getResources().getString(R.string.dd_txt_cause) + ": " + adr.cause);
                    txtSymptoms.setText(getResources().getString(R.string.dd_txt_symptom) + ": " + adr.symptoms);
                    txtPrecaution.setText(getResources().getString(R.string.dd_txt_precaution) + ": " + adr.precaution);
                    txtNatural_treatment.setText(getResources().getString(R.string.dd_txt_natural_treatment) + ": " + adr.natural_treatment);
                    txtPesticide.setText(getResources().getString(R.string.dd_txt_pesticides) + ": " + adr.pesticide);

                    String imagePath = Api.DISEASE_IMAGE_FOLDER + adr.clear_image;

                    Glide.with(DiseaseDetail.this)
                            .load(imagePath)
                            .into(clear_image);
                }else{
                    Toast.makeText(DiseaseDetail.this, "Disease not found! You " +
                            "will get expert advice soon on your mobile.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<ApiDeseaseResponse>> call, Throwable t) {

            }
        });
    }

    private void getDiseaseDetailHi(Api api, int dId){
        Call<List<ApiDeseaseResponseHi>> call = api.diseaseInfoHi(dId, tools.getPrefValue(Tools.PREF_LANG, Tools.LANG_HI));
        call.enqueue(new Callback<List<ApiDeseaseResponseHi>>() {
            @Override
            public void onResponse(Call<List<ApiDeseaseResponseHi>> call, Response<List<ApiDeseaseResponseHi>> response) {
                List<ApiDeseaseResponseHi> rs = response.body();
                ApiDeseaseResponseHi adr = rs.get(0);
                txtName.setText(getResources().getString(R.string.dd_txt_disease_name)+": "+ adr.name_hi);
                txtCause.setText(getResources().getString(R.string.dd_txt_cause)+": "+ adr.cause_hi);
                txtPrecaution.setText(getResources().getString(R.string.dd_txt_cause)+": "+adr.precaution_hi);
                txtSymptoms.setText(getResources().getString(R.string.dd_txt_precaution)+": "+adr.symptoms_hi);
                txtNatural_treatment.setText(getResources().getString(R.string.dd_txt_precaution)+": "+adr.natural_treatment_hi);
                txtPesticide.setText(getResources().getString(R.string.dd_txt_pesticides)+": "+adr.pesticide_hi);

                String imagePath = Api.DISEASE_IMAGE_FOLDER+adr.clear_image;

                Glide.with(DiseaseDetail.this)
                        .load(imagePath)
                        .into(clear_image);
            }

            @Override
            public void onFailure(Call<List<ApiDeseaseResponseHi>> call, Throwable t) {

            }
        });
    }
}