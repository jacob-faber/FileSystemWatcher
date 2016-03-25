package com.camabeh.fswatcher;

import java.io.IOException;

public interface Service {
    void start() throws IOException;
    void stop() throws IOException;
}
