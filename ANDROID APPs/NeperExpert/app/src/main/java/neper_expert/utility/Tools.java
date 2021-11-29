package sih.cvrce.neper_expert.utility;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Patterns;
import android.widget.EditText;

public class Tools {
    public static final String INTENT_EXTRA_CROP_TYPE ="crop_type";
    public static final String INTENT_EXTRA_CROP ="crop";

    public static final String PREF_NAME = "nepar_exp_pref";
    public static final String PREF_ISLOGGEDIN = "isLoggedIn";
    public static final String PREF_EXP_ID = "expert_id";
    public static final String PREF_LANG = "language";

    public static final String LANG_EN = "en";
    public static final String LANG_HI = "hi";

    private SharedPreferences sharedPreferences;

    public Tools(Context context) {
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

    public boolean validateText(EditText editText, String errorText){
        String text = editText.getText().toString().trim();
        if(text.isEmpty()){
            editText.setError(errorText);
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
}
