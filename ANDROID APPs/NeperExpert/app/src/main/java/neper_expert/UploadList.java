package sih.cvrce.neper_expert;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sih.cvrce.neper_expert.api.Api;
import sih.cvrce.neper_expert.model.CropUncategorizedList;
import sih.cvrce.neper_expert.utility.DiseaseRecyclerAdapter;
import sih.cvrce.neper_expert.utility.Tools;

public class UploadList extends AppCompatActivity {

    private RecyclerView listOfDisease;
    private Tools tools;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_list);

        listOfDisease = findViewById(R.id.list_diseases);

        tools = new Tools(this);

        listOfDisease.setLayoutManager(new LinearLayoutManager(UploadList.this));

        progressDialog = tools.createProgressDialog(UploadList.this, "Loading! Please wait");
        progressDialog.show();

        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Api api = retrofit.create(Api.class);

        Call<List<CropUncategorizedList>> call = api.getDetections(getIntent().getStringExtra(Tools.INTENT_EXTRA_CROP_TYPE));
        call.enqueue(new Callback<List<CropUncategorizedList>>() {
            @Override
            public void onResponse(Call<List<CropUncategorizedList>> call, Response<List<CropUncategorizedList>> response) {
                progressDialog.dismiss();
                List<CropUncategorizedList> rs = response.body();
                DiseaseRecyclerAdapter dradapter = new DiseaseRecyclerAdapter(rs);
                listOfDisease.setAdapter(dradapter);
            }

            @Override
            public void onFailure(Call<List<CropUncategorizedList>> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }
}
