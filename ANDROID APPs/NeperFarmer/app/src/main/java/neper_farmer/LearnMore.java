package sih.cvrce.neper_farmer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sih.cvrce.neper_farmer.api.Api;
import sih.cvrce.neper_farmer.model.ApiAllDeseaseInfo;
import sih.cvrce.neper_farmer.model.ApiAllDeseaseInfoHi;
import sih.cvrce.neper_farmer.utility.DiseaseRecyclerAdapter;
import sih.cvrce.neper_farmer.utility.DiseaseRecyclerAdapterHi;
import sih.cvrce.neper_farmer.utility.Tools;

public class LearnMore extends AppCompatActivity{

    private RecyclerView recyclerView;
    private Tools tools;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_more);

        recyclerView = findViewById(R.id.disease_data);
        recyclerView.setLayoutManager(new LinearLayoutManager(LearnMore.this));

        String cropType = getIntent().getStringExtra(Tools.INTENT_EXTRA_CROP_TYPE);

        if(cropType.equalsIgnoreCase("rice")){
            cropType = getResources().getString(R.string.hs_txt_rice);
        }else if(cropType.equalsIgnoreCase("cabbage")){
            cropType = getResources().getString(R.string.hs_txt_cabbage);
        }else if(cropType.equalsIgnoreCase("maze")){
            cropType = getResources().getString(R.string.hs_txt_maize);
        }else if(cropType.equalsIgnoreCase("cotton")){
            cropType = getResources().getString(R.string.hs_txt_cotton);
        }else if(cropType.equalsIgnoreCase("potato")){
            cropType = getResources().getString(R.string.hs_txt_potato);
        }else if(cropType.equalsIgnoreCase("wheat")){
            cropType = getResources().getString(R.string.hs_txt_wheat);
        }

        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Api api = retrofit.create(Api.class);
        tools = new Tools(this);

        if(tools.getPrefValue(Tools.PREF_LANG, Tools.LANG_EN).equalsIgnoreCase(Tools.LANG_HI)){
            getDiseaseListHi(api, cropType);
        }else{
            getDiseaseListEn(api, cropType);
        }

    }

    private void getDiseaseListEn(Api api, String cropType) {
        Call<List<ApiAllDeseaseInfo>> call = api.allDiseaseInfo(cropType, tools.getPrefValue(Tools.PREF_LANG, Tools.LANG_EN));
        call.enqueue(new Callback<List<ApiAllDeseaseInfo>>() {
            @Override
            public void onResponse(Call<List<ApiAllDeseaseInfo>> call, Response<List<ApiAllDeseaseInfo>> response) {
                List<ApiAllDeseaseInfo> rs = response.body();
                DiseaseRecyclerAdapter dradapter = new DiseaseRecyclerAdapter(rs);
                recyclerView.setAdapter(dradapter);
            }

            @Override
            public void onFailure(Call<List<ApiAllDeseaseInfo>> call, Throwable t) {

            }
        });
    }

    private void getDiseaseListHi(Api api, String cropType) {
        Call<List<ApiAllDeseaseInfoHi>> call = api.allDiseaseInfoHi(cropType, tools.getPrefValue(Tools.PREF_LANG, Tools.LANG_EN));
        call.enqueue(new Callback<List<ApiAllDeseaseInfoHi>>() {
            @Override
            public void onResponse(Call<List<ApiAllDeseaseInfoHi>> call, Response<List<ApiAllDeseaseInfoHi>> response) {
                List<ApiAllDeseaseInfoHi> rs = response.body();
                DiseaseRecyclerAdapterHi dradapter = new DiseaseRecyclerAdapterHi(rs);
                recyclerView.setAdapter(dradapter);
            }

            @Override
            public void onFailure(Call<List<ApiAllDeseaseInfoHi>> call, Throwable t) {

            }
        });
    }
}
