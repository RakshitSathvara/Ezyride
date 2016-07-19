package in.vaksys.ezyride.responces;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Harsh on 21-06-2016.
 */
public class RegisterResponse extends CommonResponse {

    /**
     * error : false
     * status : true
     * apiKey : cef6d975ea736f120095f83d8023753d
     * message : You are successfully registered, please verify your mobile number.
     */

    @SerializedName("status")
    private boolean status;
    @SerializedName("apiKey")
    private String apiKey;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

}
