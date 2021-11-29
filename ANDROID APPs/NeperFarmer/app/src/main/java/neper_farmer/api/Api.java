package sih.cvrce.neper_farmer.api;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import sih.cvrce.neper_farmer.model.ApiAllDeseaseInfo;
import sih.cvrce.neper_farmer.model.ApiAllDeseaseInfoHi;
import sih.cvrce.neper_farmer.model.ApiDeseaseResponse;
import sih.cvrce.neper_farmer.model.ApiDeseaseResponseHi;
import sih.cvrce.neper_farmer.model.ApiResponse;
import sih.cvrce.neper_farmer.model.ImageUploadResponse;

public interface Api {

    String PY_BASE_URL = "http://safwan.pythonanywhere.com/";
    String BASE_URL = "http://sgsih.000webhostapp.com/";
    String DISEASE_IMAGE_FOLDER = BASE_URL+"clear_images/";

    @GET("save_otp.php")
    Call<ApiResponse> registerFarmer(@Query("phone_no") String number);

    @GET("check_otp.php")
    Call<ApiResponse> registerOtp(@Query("phone_no") String number, @Query("otp") String otp);

    @GET("disease_info.php")
    Call<List<ApiDeseaseResponse>> diseaseInfo(@Query("disease_id") int diseaseId, @Query("lang") String lang);

    @GET("disease_info.php")
    Call<List<ApiDeseaseResponseHi>> diseaseInfoHi(@Query("disease_id") int diseaseId, @Query("lang") String lang);

    @GET("get_diseases_in_drop.php")
    Call<List<ApiAllDeseaseInfo>> allDiseaseInfo(@Query("crop") String cropName, @Query("lang") String lang);

    @GET("get_diseases_in_drop.php")
    Call<List<ApiAllDeseaseInfoHi>> allDiseaseInfoHi(@Query("crop_hi") String cropName, @Query("lang") String lang);

    //image upload to python server
    @Multipart
    @POST("upload")
    Call<ImageUploadResponse> uploadImage(@Part MultipartBody.Part file);

    //image upload to php server
    @Multipart
    @POST("upload_image.php")
    Call<ApiResponse> uploadImage(@Part MultipartBody.Part file,
                                          @Part("crop") RequestBody crop,
                                          @Part("farmer_no") RequestBody fMobile,
                                          @Part("disease_id") RequestBody diseaseId);
}