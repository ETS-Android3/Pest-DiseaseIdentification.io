package sih.cvrce.neper_farmer.utility;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Patterns;
import android.widget.EditText;

import java.util.Locale;

public class Tools {
    public static final String INTENT_EXTRA_CROP_TYPE ="crop_type";
    public static final String INTENT_EXTRA_DISEASE_ID ="diseaseId";

    public static final String PREF_NAME = "nepar_pref";
    public static final String PREF_ISLOGGEDIN = "isLoggedIn";
    public static final String PREF_MOBILE = "mobile";
    public static final String PREF_LANG = "language";

    public static final String LANG_EN = "en";
    public static final String LANG_HI = "hi";

    private SharedPreferences sharedPreferences;
    private Context context;

    public Tools(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public boolean isLoggedIn(){
        return sharedPreferences.getBoolean(PREF_ISLOGGEDIN, false);
    }

    public void savePref(String key, boolean value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public void savePref(String key, String value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getPrefValue(String key, String defValue){
        return sharedPreferences.getString(key, defValue);
    }

    public boolean validateMobile(EditText editText){
        String mobileNum = editText.getText().toString().trim();
        if(mobileNum.isEmpty() || (mobileNum.length()!=10)
                || !Patterns.PHONE.matcher(mobileNum).matches() || !containsOnlyDigits(mobileNum)){
            editText.setError("Please Enter a Valid Mobile Number");
            editText.requestFocus();
            return false;
        }else{
            return true;
        }
    }

    public boolean validateOtp(EditText editText){
        String otp = editText.getText().toString().trim();
        if(otp.isEmpty() || (otp.length()!=4) || !containsOnlyDigits(otp)){
            editText.setError("Please Enter the OTP You Have Received in SMS");
            editText.requestFocus();
            return false;
        }else{
            return true;
        }
    }

    public ProgressDialog createProgressDialog(Context context, String message){
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        return progressDialog;
    }

    public boolean containsOnlyDigits(final String value) {
        for (int i = 0; i < value.length(); i++) {
            if(!Character.isDigit(value.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }
}