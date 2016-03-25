package com.camabeh.fswatcher.output;

import com.camabeh.fswatcher.Event;

public class PrettyWriter extends Writer {
    @Override
    public void write(Event event) {
        out.println("Okay");
    }
}
