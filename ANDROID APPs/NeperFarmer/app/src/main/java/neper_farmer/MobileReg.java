package sih.cvrce.neper_farmer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sih.cvrce.neper_farmer.api.Api;
import sih.cvrce.neper_farmer.model.ApiResponse;
import sih.cvrce.neper_farmer.utility.Tools;

public class MobileReg extends AppCompatActivity {

    private EditText et_mobile;
    private Button btn_submit;

    private Tools tools;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_reg);

        btn_submit = findViewById(R.id.button);
        et_mobile = findViewById(R.id.editText);

        tools = new Tools(this);
        progressDialog = tools.createProgressDialog(this, "Authenticating. Please wait.");

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tools.validateMobile(et_mobile)){
                    String mobile = et_mobile.getText().toString().trim();
                    registerUser(mobile);
                }
            }
        });
    }

    private void registerUser(final String mobileNum){
        progressDialog.show();

        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Api api = retrofit.create(Api.class);

        Call<ApiResponse> call = api.registerFarmer(mobileNum);

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                progressDialog.dismiss();
                if(response.isSuccessful() && response.body().Status.equals("Success")){
                    tools.savePref(Tools.PREF_MOBILE, mobileNum);
                    Intent intent = new Intent(MobileReg.this, ValidateOtp.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(MobileReg.this, response.code()+": "+response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(MobileReg.this, t.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
    }
}