package in.vaksys.ezyride.responces;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Harsh on 24-06-2016.
 */
public class CarResponse extends CommonResponse {


    /**
     * error : false
     * cars : [{"id":0,"car_model":"select"},{"id":"1","car_model":"Honda civic"},{"id":"2","car_model":"Honda Shine"},{"id":156415642,"car_model":"Add new Car"}]
     */

    /**
     * id : 0
     * car_model : select
     */

    @SerializedName("cars")
    private List<CarsEntity> cars;

    /**
     * message : No cars found.
     */


    public List<CarsEntity> getCars() {
        return cars;
    }

    public void setCars(List<CarsEntity> cars) {
        this.cars = cars;
    }


    public static class CarsEntity {
        @SerializedName("id")
        private int id;
        @SerializedName("car_model")
        private String carModel;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCarModel() {
            return carModel;
        }

        public void setCarModel(String carModel) {
            this.carModel = carModel;
        }
    }
}
