package org.example;


import org.example.pipe.CsvWriter;
import org.example.pipe.JsonReader;
import org.example.pipe.JsonToCsvConverter;
import org.example.pipe.Pipe;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Pipe pipe = new JsonReader(new JsonToCsvConverter(new CsvWriter()));
        pipe.dataFlow("input.json");
    }
}
