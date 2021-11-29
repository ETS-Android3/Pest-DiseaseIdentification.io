package sih.cvrce.neper_expert;

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
import sih.cvrce.neper_expert.api.Api;
import sih.cvrce.neper_expert.model.ApiResponse;
import sih.cvrce.neper_expert.utility.Tools;

public class Login extends AppCompatActivity {

    private EditText et_userid, et_password;
    private Button btn_signin;

    private Tools tools;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_userid = findViewById(R.id.et_userId);
        et_password = findViewById(R.id.et_password);
        btn_signin = findViewById(R.id.btn_login);

        tools = new Tools(Login.this);
        progressDialog = tools.createProgressDialog(Login.this, "Authenticating. Please wait.");

        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tools.validateText(et_userid, "Please Enter Your User Name")
                        && tools.validateText(et_password, "Please Enter Your Password")){
                    String userId = et_userid.getText().toString().trim();
                    String password = et_password.getText().toString().trim();
                    registerUser(userId, password);
                }
            }
        });
    }

    private void registerUser(String userId, String password) {
        progressDialog.show();

        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Api api = retrofit.create(Api.class);

        Call<ApiResponse> call = api.expertLogin(userId, password);

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                progressDialog.dismiss();
                if(response.isSuccessful() && response.body().Status.equals("Success")){
                    tools.savePref(Tools.PREF_ISLOGGEDIN, true);
                    Intent intent = new Intent(Login.this, ExpertHome.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(Login.this, response.code()+": "+response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(Login.this, t.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
    }
}