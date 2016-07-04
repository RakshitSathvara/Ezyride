package in.vaksys.ezyride.responces;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Harsh on 22-06-2016.
 */
public class OTPResponce extends CommonResponse {


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

}
