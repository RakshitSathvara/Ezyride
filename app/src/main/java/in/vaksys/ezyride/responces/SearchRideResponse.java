package in.vaksys.ezyride.responces;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Harsh on 05-07-2016.
 */
public class SearchRideResponse extends CommonResponse{


    /**
     * id : 2
     * user_id : 4
     * user_image : http://vakratundasystem.in/ezyride/assets/uploads/Profile_1467639030.jpg
     * username : Ezyride Ezyride
     * car_id : 6
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
     * creation_time : 2016-07-01 05:20:12
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
        private String id;
        @SerializedName("user_id")
        private String userId;
        @SerializedName("user_image")
        private String userImage;
        @SerializedName("username")
        private String username;
        @SerializedName("car_id")
        private String carId;
        @SerializedName("from_lat")
        private String fromLat;
        @SerializedName("from_long")
        private String fromLong;
        @SerializedName("to_lat")
        private String toLat;
        @SerializedName("to_long")
        private String toLong;
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
        private String pricePerSeat;
        @SerializedName("seat_availability")
        private String seatAvailability;
        @SerializedName("only_ladies")
        private String onlyLadies;
        @SerializedName("creation_time")
        private String creationTime;
        @SerializedName("updation_time")
        private String updationTime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
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

        public String getCarId() {
            return carId;
        }

        public void setCarId(String carId) {
            this.carId = carId;
        }

        public String getFromLat() {
            return fromLat;
        }

        public void setFromLat(String fromLat) {
            this.fromLat = fromLat;
        }

        public String getFromLong() {
            return fromLong;
        }

        public void setFromLong(String fromLong) {
            this.fromLong = fromLong;
        }

        public String getToLat() {
            return toLat;
        }

        public void setToLat(String toLat) {
            this.toLat = toLat;
        }

        public String getToLong() {
            return toLong;
        }

        public void setToLong(String toLong) {
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

        public String getPricePerSeat() {
            return pricePerSeat;
        }

        public void setPricePerSeat(String pricePerSeat) {
            this.pricePerSeat = pricePerSeat;
        }

        public String getSeatAvailability() {
            return seatAvailability;
        }

        public void setSeatAvailability(String seatAvailability) {
            this.seatAvailability = seatAvailability;
        }

        public String getOnlyLadies() {
            return onlyLadies;
        }

        public void setOnlyLadies(String onlyLadies) {
            this.onlyLadies = onlyLadies;
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
