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
            String title = feature.get("title").asText();
            title.replaceAll("\"", "'");
            String categoryName = feature.get("categoryName").asText();
            String address = feature.get("address").asText().replaceAll(","," ");
            String street = feature.get("street").asText().replaceAll(","," ");
            String city = feature.get("city").asText();
            String postalCode = feature.get("postalCode").asText();
            String countryCode = feature.get("countryCode").asText();
            String website = feature.get("website").asText();
            String phone = feature.get("phone").asText();

            StringBuilder location = new StringBuilder();

            location.append(feature1.get("lat").asText());
            location.append(", ");
            location.append(feature1.get("lng").asText());

            String plusCode = feature.get("plusCode").asText();


            printer.printRecord(title, categoryName,address,  street, city, countryCode, website, phone, location.toString().replaceAll(","," "), plusCode);
        }

        printer.close();
    }
}
