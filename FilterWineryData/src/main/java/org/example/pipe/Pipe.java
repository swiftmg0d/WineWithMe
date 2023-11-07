package org.example.pipe;

import java.io.IOException;

public interface Pipe {
    void dataFlow(Object data) throws IOException;
}
