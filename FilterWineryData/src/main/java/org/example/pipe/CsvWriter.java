package org.example.pipe;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.csv.CSVPrinter;
import org.example.pipe.Pipe;

import java.io.IOException;
import java.util.Iterator;

public class CsvWriter implements Pipe {
    @Override
    public void dataFlow(Object data) throws IOException {
        Object[] objects = (Object[]) data;
        JsonNode jsonNode = (JsonNode) objects[0];
        CSVPrinter printer = (CSVPrinter) objects[1];


        Iterator<JsonNode> elements = jsonNode.elements();

        while (elements.hasNext()) {
            JsonNode feature = elements.next();
            JsonNode feature1 = feature.get("location");
            if(!(feature.get("categoryName").asText().toLowerCase().contains("winery") )) continue;
            String title = feature.get("title").asText();
            title.replaceAll("“","");
            StringBuilder title1 = new StringBuilder();
            title1.append(title);
            String categoryName = feature.get("categoryName").asText();
            String address = feature.get("address").asText();
            String street = feature.get("street").asText();
            String city = feature.get("city").asText();
            String postalCode = feature.get("postalCode").asText();
            String countryCode = feature.get("countryCode").asText();
            String website = feature.get("website").asText();
            String phone = feature.get("phone").asText();
            String totalScore = feature.get("totalScore").asText().equals("null") ? "0" : feature.get("totalScore").asText();
            String reviewsCount = feature.get("reviewsCount").asText();
            StringBuilder location = new StringBuilder();

            location.append(feature1.get("lat").asText());
            location.append(", ");
            location.append(feature1.get("lng").asText());

            String plusCode = feature.get("plusCode").asText();


            printer.printRecord(title1, categoryName, address, street, city, countryCode, website, phone, location.toString(), plusCode, postalCode, totalScore, reviewsCount);
        }

        printer.close();
    }
}
