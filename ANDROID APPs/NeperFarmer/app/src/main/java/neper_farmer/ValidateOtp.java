package sih.cvrce.neper_farmer;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sih.cvrce.neper_farmer.api.Api;
import sih.cvrce.neper_farmer.model.ApiResponse;
import sih.cvrce.neper_farmer.utility.Tools;

public class ValidateOtp extends AppCompatActivity {

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1000;

    private EditText otpText;
    private Button requestOtp, submitOtp;

    private Tools tools;
    private ProgressDialog progressDialog;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {
                final String message = intent.getStringExtra("message");
                otpText.setText(message.trim());
                otpText.setCursorVisible(false);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validate_otp);

        otpText = findViewById(R.id.otptext);
        requestOtp = findViewById(R.id.request_otp);
        submitOtp = findViewById(R.id.submitotp);

        tools = new Tools(this);

        checkAndRequestPermissions();

        requestOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = tools.createProgressDialog(ValidateOtp.this, "Requesting for new OTP. Please wait.");
                requestOtp(tools.getPrefValue(Tools.PREF_MOBILE, "0"));
            }
        });

        submitOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tools.validateOtp(otpText)){
                    progressDialog = tools.createProgressDialog(ValidateOtp.this, "Submitting OTP. Please wait.");
                    String otp = otpText.getText().toString().trim();
                    registerUser(otp);
                }
            }
        });
    }

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    private  void checkAndRequestPermissions() {
        int receiveSMS = ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECEIVE_SMS);

        int readSMS = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_SMS);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (receiveSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECEIVE_MMS);
        }
        if (readSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_SMS);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
        }
    }

    private void requestOtp(final String mobileNum){
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
                    Toast.makeText(ValidateOtp.this, "Request for a new OTP is made successfully!", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(ValidateOtp.this, response.code()+": "+response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(ValidateOtp.this, t.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
    }

    private void registerUser(String otp){
        progressDialog.show();

        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Api api = retrofit.create(Api.class);

        Call<ApiResponse> call = api.registerOtp(tools.getPrefValue(Tools.PREF_MOBILE, "0"), otp);

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                progressDialog.dismiss();
                if(response.isSuccessful() && response.body().Status.equals("Success")){
                    tools.savePref(Tools.PREF_ISLOGGEDIN, true);
                    Intent intent = new Intent(ValidateOtp.this, HomeScreen.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(ValidateOtp.this, response.code()+": "+response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(ValidateOtp.this, t.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
    }
}
