package sih.cvrce.neper_farmer;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sih.cvrce.neper_farmer.api.Api;
import sih.cvrce.neper_farmer.model.ApiResponse;
import sih.cvrce.neper_farmer.model.ImageUploadResponse;
import sih.cvrce.neper_farmer.utility.Tools;

public class ImageUpload extends AppCompatActivity implements View.OnClickListener {

    private static final int IMG_CAP = 987;
    private static final int IMAGE_PICK = 988;

    private ImageView cropImage;
//    private Uri photoUri;
    private File imgFile;
    private Tools tools;

    private String mCropType;
    private int dId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload);

        findViewById(R.id.btn_capture).setOnClickListener(this);
        findViewById(R.id.btn_gallery).setOnClickListener(this);
        findViewById(R.id.btn_upload).setOnClickListener(this);
        findViewById(R.id.btn_learnmore).setOnClickListener(this);

        cropImage = findViewById(R.id.crop_image);

        tools = new Tools(this);

        mCropType = getIntent().getStringExtra(Tools.INTENT_EXTRA_CROP_TYPE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_capture:
                captureImage();
                break;
            case R.id.btn_gallery:
                pickFromGallery();
                break;
            case R.id.btn_upload:
//                if(photoUri!=null) {
//                    uploadFile(photoUri, mCropType);
                if(imgFile!=null){
//                    startDetailActivity(1);
                    uploadFile(imgFile, mCropType);
                }else{
                    Toast.makeText(ImageUpload.this, "Please capture or select an image first", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btn_learnmore:
                Intent intent = new Intent(ImageUpload.this, LearnMore.class);
                intent.putExtra(Tools.INTENT_EXTRA_CROP_TYPE, mCropType);
                startActivity(intent);
                break;
        }
    }

    private void startDetailActivity(int diseaseId){
        Intent intent = new Intent(ImageUpload.this, DiseaseDetail.class);
        intent.putExtra(Tools.INTENT_EXTRA_DISEASE_ID, diseaseId);
        startActivity(intent);
        finish();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if(requestCode==IMG_CAP){
                cropImage.setImageURI(Uri.fromFile(imgFile));
            }
            if(requestCode == IMAGE_PICK){
                Uri _photoUri = data.getData();
                cropImage.setImageURI(_photoUri);

                try {
                    String path = getAbsolutePathOfImage(_photoUri);
                    File file = new File(path);
                    File destinationFile = createImage();
                    copyFile(file, destinationFile);
//                    photoUri = Uri.fromFile(destinationFile);
                    imgFile = destinationFile;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else{
            Toast.makeText(ImageUpload.this, "No Image Data", Toast.LENGTH_LONG).show();
        }
    }

    private void pickFromGallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_PICK);
    }

    private File createImage() throws IOException {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = tools.getPrefValue(Tools.PREF_MOBILE, "0")+"_"+timestamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,".jpg", storageDir);
        return image;
    }

    private void captureImage(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED){
            Intent pIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:"+getPackageName()));
            startActivity(pIntent);
            return;
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(intent.resolveActivity(getPackageManager())!=null) {
            File photo = null;
            try{
                photo = createImage();
                imgFile = photo;
            }catch (IOException e){
                e.printStackTrace();
            }

            if(photo!=null) {
                Uri photoUri = FileProvider.getUriForFile(this, "sih.cvrce.neper_farmer.fileprovider", photo);

                List<ResolveInfo> resolvedIntentActivities = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

                for (ResolveInfo resolvedIntentInfo : resolvedIntentActivities) {
                    String packageName = resolvedIntentInfo.activityInfo.packageName;

                    grantUriPermission(packageName, photoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }

                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, IMG_CAP);
            }
        }
    }

    private String getAbsolutePathOfImage(Uri imageUri){
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, imageUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int col_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(col_index);
        cursor.close();
        return result;
    }

    private void uploadFile(File uFile, String _cropType){
        final ProgressDialog progressDialog = tools.createProgressDialog(this, "Uploading the image.");
        progressDialog.show();

        final RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), uFile);
        final MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", uFile.getName(), requestFile);
        final RequestBody cropType = RequestBody.create(MediaType.parse("text/plain"), _cropType);
        final RequestBody mobile = RequestBody.create(MediaType.parse("text/plain"), tools.getPrefValue(Tools.PREF_MOBILE, "0"));

        final Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.PY_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Api api = retrofit.create(Api.class);

        Call<ImageUploadResponse> call = api.uploadImage(body);

        call.enqueue(new Callback<ImageUploadResponse>() {
            @Override
            public void onResponse(Call<ImageUploadResponse> call, Response<ImageUploadResponse> response) {
                if(response.isSuccessful()) {
                    dId = Integer.parseInt(response.body().disease_id);
                    RequestBody diseaseId = RequestBody.create(MediaType.parse("text/plain"), response.body().disease_id);
                    uploadToPhp(body, cropType, mobile, diseaseId, gson, progressDialog);
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(ImageUpload.this, response.code()+": "+response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ImageUploadResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ImageUpload.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void uploadToPhp(MultipartBody.Part body, RequestBody cropType, RequestBody mobile,
                             final RequestBody diseaseId, Gson gson,
                             final ProgressDialog progressDialog){

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        Api api = retrofit.create(Api.class);

        Call<ApiResponse> callPhp = api.uploadImage(body, cropType, mobile, diseaseId);
        callPhp.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                progressDialog.dismiss();
                startDetailActivity(dId);
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                progressDialog.dismiss();
                startDetailActivity(dId);
            }
        });
    }

    private void copyFile(File sourceFile, File destFile) throws IOException {
        if (!sourceFile.exists()) {
            return;
        }

        FileChannel source = null;
        FileChannel destination = null;
        source = new FileInputStream(sourceFile).getChannel();
        destination = new FileOutputStream(destFile).getChannel();
        if (destination != null && source != null) {
            destination.transferFrom(source, 0, source.size());
        }
        if (source != null) {
            source.close();
        }
        if (destination != null) {
            destination.close();
        }
    }
}