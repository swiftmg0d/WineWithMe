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
    private String Title;
    private String CategoryName;
    private String Address;
    private String Street;
    private String city;
    private String CountryCode;
    private String Website;
    private String Phone;
    private String Location;
    private String PlusCode;
    private String postalCode;
    private String totalScore;
    private String reviewsCount;


    public Winery() {
    }

    public Winery(String title, String categoryName, String address, String street, String city, String countryCode, String website, String phone, String location, String plusCode, String postalCode, String totalScore, String reviewsCount) {
        Title = title;
        CategoryName = categoryName;
        Address = address;
        Street = street;
        this.city = city;
        CountryCode = countryCode;
        Website = website;
        Phone = phone;
        Location = location;
        PlusCode = plusCode;
        this.postalCode = postalCode;
        this.totalScore = totalScore;
        this.reviewsCount = reviewsCount;
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
