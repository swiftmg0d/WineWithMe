package mk.finki.ukim.dians.winewithme.model;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor

public class Winery {
    private String name;
    private String address;
    private String street;
    private String city;
    private String countryCode;
    private String webSite;
    private String phone;
    private String location;
    private String plusCode;

    public static Winery createWinery(String line) {
        String[] splited = line.split(",");
        return new Winery(splited[0],
                splited[1].contains("null") ? "Not mention" : splited[1],
                splited[2].contains("null") ? "Not mention" : splited[2],
                splited[3].contains("null") ? "Not mention" : splited[3],
                splited[4].contains("null") ? "Not mention" : splited[4],
                splited[5].contains("null") ? "Not mention" : splited[5],
                splited[6].contains("null") ? "Not mention" : splited[6],
                splited[7].contains("null") ? "Not mention" : splited[7],
                splited[8].contains("null") ? "Not mention" : splited[8]
                );
    }
}
