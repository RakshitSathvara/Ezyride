package in.vaksys.ezyride.responces;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Harsh on 05-07-2016.
 */
public class SearchRideResponse extends CommonResponse {


    /**
     * id : 2
     * user_id : 17
     * user_image : http://vakratundasystem.in/ezyride/assets/uploads/Profile_1467639030.jpg
     * username : milan
     * car_id : 4
     * from_lat : 21.170240099999997
     * from_long : 72.83106070000001
     * to_lat : 19.0759837
     * to_long : 72.8776559
     * from_main_address : Surat
     * from_sub_address : Surat, Gujarat, India
     * to_main_address : Mumbai
     * to_sub_address : Mumbai, Maharashtra, India
     * ride_date : 2016-07-15
     * ride_time : 00:00:16
     * price_per_seat : 1000
     * seat_availability : 3
     * only_ladies : 1
     * car_image : http://vakratundasystem.in/ezyride/assets/uploads/1467365860.jpg
     * seat_belt : 1
     * air_bag : 0
     * ac_availability : 1
     * music_system : 1
     * car_layout : 5
     * car_model : honda city
     * pan_verify : 0
     * corp_email_verify : 0
     * contact_verify : 1
     * age : 26
     * creation_time : 2016-06-30 23:50:12
     * updation_time : 0000-00-00 00:00:00
     */

    @SerializedName("rides")
    private List<RidesEntity> rides;

    public List<RidesEntity> getRides() {
        return rides;
    }

    public void setRides(List<RidesEntity> rides) {
        this.rides = rides;
    }

    public static class RidesEntity {
        @SerializedName("id")
        private int id;
        @SerializedName("user_id")
        private int userId;
        @SerializedName("user_image")
        private String userImage;
        @SerializedName("username")
        private String username;
        @SerializedName("car_id")
        private int carId;
        @SerializedName("from_lat")
        private double fromLat;
        @SerializedName("from_long")
        private double fromLong;
        @SerializedName("to_lat")
        private double toLat;
        @SerializedName("to_long")
        private double toLong;
        @SerializedName("from_main_address")
        private String fromMainAddress;
        @SerializedName("from_sub_address")
        private String fromSubAddress;
        @SerializedName("to_main_address")
        private String toMainAddress;
        @SerializedName("to_sub_address")
        private String toSubAddress;
        @SerializedName("ride_date")
        private String rideDate;
        @SerializedName("ride_time")
        private String rideTime;
        @SerializedName("price_per_seat")
        private int pricePerSeat;
        @SerializedName("seat_availability")
        private int seatAvailability;
        @SerializedName("only_ladies")
        private int onlyLadies;
        @SerializedName("car_image")
        private String carImage;
        @SerializedName("seat_belt")
        private int seatBelt;
        @SerializedName("air_bag")
        private int airBag;
        @SerializedName("ac_availability")
        private int acAvailability;
        @SerializedName("music_system")
        private int musicSystem;
        @SerializedName("car_layout")
        private int carLayout;
        @SerializedName("car_model")
        private String carModel;
        @SerializedName("pan_verify")
        private int panVerify;
        @SerializedName("corp_email_verify")
        private int corpEmailVerify;
        @SerializedName("contact_verify")
        private int contactVerify;
        @SerializedName("age")
        private int age;
        @SerializedName("creation_time")
        private String creationTime;
        @SerializedName("updation_time")
        private String updationTime;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getUserImage() {
            return userImage;
        }

        public void setUserImage(String userImage) {
            this.userImage = userImage;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public int getCarId() {
            return carId;
        }

        public void setCarId(int carId) {
            this.carId = carId;
        }

        public double getFromLat() {
            return fromLat;
        }

        public void setFromLat(double fromLat) {
            this.fromLat = fromLat;
        }

        public double getFromLong() {
            return fromLong;
        }

        public void setFromLong(double fromLong) {
            this.fromLong = fromLong;
        }

        public double getToLat() {
            return toLat;
        }

        public void setToLat(double toLat) {
            this.toLat = toLat;
        }

        public double getToLong() {
            return toLong;
        }

        public void setToLong(double toLong) {
            this.toLong = toLong;
        }

        public String getFromMainAddress() {
            return fromMainAddress;
        }

        public void setFromMainAddress(String fromMainAddress) {
            this.fromMainAddress = fromMainAddress;
        }

        public String getFromSubAddress() {
            return fromSubAddress;
        }

        public void setFromSubAddress(String fromSubAddress) {
            this.fromSubAddress = fromSubAddress;
        }

        public String getToMainAddress() {
            return toMainAddress;
        }

        public void setToMainAddress(String toMainAddress) {
            this.toMainAddress = toMainAddress;
        }

        public String getToSubAddress() {
            return toSubAddress;
        }

        public void setToSubAddress(String toSubAddress) {
            this.toSubAddress = toSubAddress;
        }

        public String getRideDate() {
            return rideDate;
        }

        public void setRideDate(String rideDate) {
            this.rideDate = rideDate;
        }

        public String getRideTime() {
            return rideTime;
        }

        public void setRideTime(String rideTime) {
            this.rideTime = rideTime;
        }

        public int getPricePerSeat() {
            return pricePerSeat;
        }

        public void setPricePerSeat(int pricePerSeat) {
            this.pricePerSeat = pricePerSeat;
        }

        public int getSeatAvailability() {
            return seatAvailability;
        }

        public void setSeatAvailability(int seatAvailability) {
            this.seatAvailability = seatAvailability;
        }

        public int getOnlyLadies() {
            return onlyLadies;
        }

        public void setOnlyLadies(int onlyLadies) {
            this.onlyLadies = onlyLadies;
        }

        public String getCarImage() {
            return carImage;
        }

        public void setCarImage(String carImage) {
            this.carImage = carImage;
        }

        public int getSeatBelt() {
            return seatBelt;
        }

        public void setSeatBelt(int seatBelt) {
            this.seatBelt = seatBelt;
        }

        public int getAirBag() {
            return airBag;
        }

        public void setAirBag(int airBag) {
            this.airBag = airBag;
        }

        public int getAcAvailability() {
            return acAvailability;
        }

        public void setAcAvailability(int acAvailability) {
            this.acAvailability = acAvailability;
        }

        public int getMusicSystem() {
            return musicSystem;
        }

        public void setMusicSystem(int musicSystem) {
            this.musicSystem = musicSystem;
        }

        public int getCarLayout() {
            return carLayout;
        }

        public void setCarLayout(int carLayout) {
            this.carLayout = carLayout;
        }

        public String getCarModel() {
            return carModel;
        }

        public void setCarModel(String carModel) {
            this.carModel = carModel;
        }

        public int getPanVerify() {
            return panVerify;
        }

        public void setPanVerify(int panVerify) {
            this.panVerify = panVerify;
        }

        public int getCorpEmailVerify() {
            return corpEmailVerify;
        }

        public void setCorpEmailVerify(int corpEmailVerify) {
            this.corpEmailVerify = corpEmailVerify;
        }

        public int getContactVerify() {
            return contactVerify;
        }

        public void setContactVerify(int contactVerify) {
            this.contactVerify = contactVerify;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getCreationTime() {
            return creationTime;
        }

        public void setCreationTime(String creationTime) {
            this.creationTime = creationTime;
        }

        public String getUpdationTime() {
            return updationTime;
        }

        public void setUpdationTime(String updationTime) {
            this.updationTime = updationTime;
        }
    }
}
