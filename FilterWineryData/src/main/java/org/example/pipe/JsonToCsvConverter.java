package org.example.pipe;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.example.pipe.Pipe;

import java.io.FileWriter;
import java.io.IOException;

public class JsonToCsvConverter implements Pipe {
    private Pipe nextPipe;

    public JsonToCsvConverter(Pipe nextPipe) {
        this.nextPipe = nextPipe;
    }

    @Override
    public void dataFlow(Object data) throws IOException {
        JsonNode jsonNode = (JsonNode) data;
        FileWriter out = new FileWriter("output.csv");
        CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT
                .withHeader("Title", "CategoryName", "Address", "Street", "City", "CountryCode", "Website", "Phone", "Location", "PlusCode"));
        nextPipe.dataFlow(new Object[]{jsonNode, printer});
    }
}
