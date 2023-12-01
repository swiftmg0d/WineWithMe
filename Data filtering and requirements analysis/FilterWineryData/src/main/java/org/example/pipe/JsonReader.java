package org.example.pipe;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.pipe.Pipe;

import java.io.FileReader;
import java.io.IOException;

public class JsonReader implements Pipe {
    private Pipe nextPipe;

    public JsonReader(Pipe nextPipe) {
        this.nextPipe = nextPipe;
    }

    @Override
    public void dataFlow(Object data) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(new FileReader((String) data));
        nextPipe.dataFlow(jsonNode);
    }
}
