package in.vaksys.ezyride.responces;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Harsh on 21-06-2016.
 */
public class RegisterResponse {

    /**
     * error : false
     * status : true
     * apiKey : cef6d975ea736f120095f83d8023753d
     * message : You are successfully registered, please verify your mobile number.
     */

    @SerializedName("error")
    private boolean error;
    @SerializedName("status")
    private boolean status;
    @SerializedName("apiKey")
    private String apiKey;
    @SerializedName("message")
    private String message;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
