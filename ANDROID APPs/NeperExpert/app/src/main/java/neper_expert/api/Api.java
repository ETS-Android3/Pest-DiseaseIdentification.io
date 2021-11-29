package sih.cvrce.neper_expert.api;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import sih.cvrce.neper_expert.model.ApiResponse;
import sih.cvrce.neper_expert.model.CropUncategorizedList;

public interface Api {

    String PY_BASE_URL = "http://safwan.pythonanywhere.com/";
    String BASE_URL = "http://sgsih.000webhostapp.com/";
    String DISEASE_IMAGE_FOLDER = BASE_URL+"clear_images/";
    String UPLOADED_IMAGE_FOLDER = BASE_URL+"uploaded_images/";

    @GET("signin.php")
    Call<ApiResponse> expertLogin(@Query("un") String userName, @Query("pw") String password);

    @GET("get_detections.php")
    Call<List<CropUncategorizedList>> getDetections(@Query("crop") String crop);

    @GET("save_detection.php")
    Call<ApiResponse> saveDetection(@Query("image_id") String imageId, @Query("correct_choise") String correctChoice);

//    @GET("get_diseases_in_drop.php")
//    Call<ApiAllDeseaseInfo> allDiseaseInfo(@Query("crop") String cropName, @Query("lang") String lang);
//
//    @Multipart
//    @POST("upload")
//    Call<ImageUploadResponse> uploadImage(@Part MultipartBody.Part file,
//                                          @Part("crop") RequestBody crop,
//                                          @Part("farmer_no") RequestBody mobile);
}