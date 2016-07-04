package in.vaksys.ezyride.utils;

/**
 * Created by Harsh on 04-07-2016.
 */
public class UploadImageCallBack {
    private String ImageURL;

    private final boolean imageType;

    public UploadImageCallBack(String imageURL, boolean imageType) {
        ImageURL = imageURL;
        this.imageType = imageType;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public boolean getImageType() {
        return imageType;
    }
}
