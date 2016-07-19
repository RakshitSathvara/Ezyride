package in.vaksys.ezyride.responces;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Harsh on 01-07-2016.
 */
public class MyCarResponse extends CommonResponse {

    /**
     * id : 1
     * car_no : 5555
     * car_model : honda city
     * car_layout : 7
     * car_image : Hydrangeas.jpg
     * ac_availability : y
     * music_system : y
     * air_bag : n
     * seat_belt : n
     */

    @SerializedName("cars")
    private List<CarsEntity> cars;

    public List<CarsEntity> getCars() {
        return cars;
    }

    public void setCars(List<CarsEntity> cars) {
        this.cars = cars;
    }

    public static class CarsEntity {
        @SerializedName("id")
        private int id;
        @SerializedName("car_no")
        private String carNo;
        @SerializedName("car_model")
        private String carModel;
        @SerializedName("car_layout")
        private int carLayout;
        @SerializedName("car_image")
        private String carImage;
        @SerializedName("ac_availability")
        private int acAvailability;
        @SerializedName("music_system")
        private int musicSystem;
        @SerializedName("air_bag")
        private int airBag;
        @SerializedName("seat_belt")
        private int seatBelt;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCarNo() {
            return carNo;
        }

        public void setCarNo(String carNo) {
            this.carNo = carNo;
        }

        public String getCarModel() {
            return carModel;
        }

        public void setCarModel(String carModel) {
            this.carModel = carModel;
        }

        public int getCarLayout() {
            return carLayout;
        }

        public void setCarLayout(int carLayout) {
            this.carLayout = carLayout;
        }

        public String getCarImage() {
            return carImage;
        }

        public void setCarImage(String carImage) {
            this.carImage = carImage;
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

        public int getAirBag() {
            return airBag;
        }

        public void setAirBag(int airBag) {
            this.airBag = airBag;
        }

        public int getSeatBelt() {
            return seatBelt;
        }

        public void setSeatBelt(int seatBelt) {
            this.seatBelt = seatBelt;
        }
    }
}
