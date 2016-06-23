package in.vaksys.ezyride.responces;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Harsh on 22-06-2016.
 */
public class OTPResponce {


    /**
     * error : false
     * fname : Harsh
     * lname : Dalwadi
     * email : harsh@vakratundasystem.in
     * mobile : 7878348267
     * dob : 23-06-2016
     * gender : 1
     * message : User successfully activated
     */

    @SerializedName("error")
    private boolean error;
    @SerializedName("fname")
    private String fname;
    @SerializedName("lname")
    private String lname;
    @SerializedName("email")
    private String email;
    @SerializedName("mobile")
    private String mobile;
    @SerializedName("dob")
    private String dob;
    @SerializedName("gender")
    private int gender;
    @SerializedName("message")
    private String message;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
