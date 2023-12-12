package mk.finki.ukim.dians.winewithme.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Entity
public class Winery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String address;
    private String street;
    private String city;
    private String countryCode;
    private String webSite;
    private String phone;
    private String location;
    private String plusCode;

    public Winery(String name, String address, String street, String city, String countryCode, String webSite, String phone, String location, String plusCode) {
        this.name = name;
        this.address = address;
        this.street = street;
        this.city = city;
        this.countryCode = countryCode;
        this.webSite = webSite;
        this.phone = phone;
        this.location = location;
        this.plusCode = plusCode;
    }

    public Winery() {
    }

    public static Winery createWinery(String line) {
        String[] splited = line.split(",");
        return new Winery(splited[0],
                splited[2],
                splited[3],
                splited[4],
                splited[5],
                splited[6],
                splited[7],
                splited[8],
                splited[9]
        );
    }
}
