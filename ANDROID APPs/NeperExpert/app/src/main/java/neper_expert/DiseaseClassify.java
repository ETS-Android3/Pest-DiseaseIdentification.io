package sih.cvrce.neper_expert;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sih.cvrce.neper_expert.api.Api;
import sih.cvrce.neper_expert.model.ApiResponse;
import sih.cvrce.neper_expert.model.CropUncategorizedList;
import sih.cvrce.neper_expert.utility.Tools;

public class DiseaseClassify extends AppCompatActivity {

//    private CropUncategorizedList list;
    private int disease;
    private String image_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_classify);

        ImageView iv=(ImageView)findViewById(R.id.imageView2);
        TextView tv=(TextView)findViewById(R.id.textView);
        Spinner s=(Spinner)findViewById(R.id.spinner);
        Button btn=(Button)findViewById(R.id.btn_submit);
        EditText ed=(EditText)findViewById(R.id.edit_text);

//        list = (CropUncategorizedList) getIntent().getSerializableExtra(Tools.INTENT_EXTRA_CROP);

        image_id = getIntent().getStringExtra(Tools.INTENT_EXTRA_CROP);

        Glide.with(this)
                .load(Api.UPLOADED_IMAGE_FOLDER+image_id)
                .into(iv);

        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                disease = position+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Update_disease();
            }
        });


    }
    public void Update_disease(){
        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Api api = retrofit.create(Api.class);

        Call<ApiResponse> call = api.saveDetection(image_id, disease+"");
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Toast.makeText(DiseaseClassify.this, "Successfully Submitted.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(DiseaseClassify.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
