package mk.finki.ukim.dians.winewithme.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Winery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private String categoryName;
    private String address;
    private String street;
    private String city;
    private String countryCode;
    private String website;
    private String phone;
    private String location;
    private String plusCode;
    private String postalCode;
    private String totalScore;
    private String reviewsCount;


    public Winery() {
    }

    public Winery(String title, String categoryName, String address, String street, String city, String countryCode, String website, String phone, String location, String plusCode, String postalCode, String totalScore, String reviewsCount) {
        this.title = title;
        this.categoryName = categoryName;
        this.address = address;
        this.street = street;
        this.city = city;
        this.countryCode = countryCode;
        this.website = website;
        this.phone = phone;
        this.location = location;
        this.plusCode = plusCode;
        this.postalCode = postalCode;
        this.totalScore = totalScore;
        this.reviewsCount = reviewsCount;
    }
    public Double scoreCalculate(){
        return Double.valueOf(totalScore)*Double.valueOf(reviewsCount);
    }

    public static Winery createWinery(String line) {
        String[] splited = line.split(",");


        return new Winery(
                splited[0],
                splited[1].contains("null") ? "Not mention!" : splited[1],
                splited[2].contains("null") ? "Not mention!" : splited[2],
                splited[3].contains("null") ? "Not mention!" : splited[3],
                splited[4].contains("null") ? "Not mention!" : splited[4],
                splited[5].contains("null") ? "Not mention!" : splited[5],
                splited[6].contains("null") ? "Not mention!" : splited[6],
                splited[7].contains("null") ? "Not mention!" : splited[7],
                splited[8].contains("null") ? "Not mention!" : splited[8],
                splited[9].contains("null") ? "Not mention!" : splited[9],
                splited[10].contains("null") ? "Not mention!" : splited[10],
                splited[11].contains("null") ? "Not mention!" : splited[11],
                splited[12].contains("null") ? "Not mention!" : splited[12]

                );


    }
}
