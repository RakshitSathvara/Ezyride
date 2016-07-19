package in.vaksys.ezyride.responces;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Harsh on 27-06-2016.
 */
public class ImageUploadResponse extends OfferRideResponse {

    /**
     * url : http://vakratundasystem.in/Ezyride/assets/uploads/Lighthouse.jpg
     */

    @SerializedName("url")
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
