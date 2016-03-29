package com.camabeh.fswatcher.output;

import com.camabeh.fswatcher.Event;
import org.fusesource.jansi.AnsiConsole;

import static org.fusesource.jansi.Ansi.Color.*;
import static org.fusesource.jansi.Ansi.ansi;

public class PrettyWriter extends Writer {

    public PrettyWriter() {
        clearConsole();
        AnsiConsole.systemInstall();
    }

    @Override
    public void write(Event e) {
        out.println(
                ansi()
                        .fg(CYAN).a(e.getTimestamp().toString())
                        .fg(MAGENTA).a(" " + e.getEventType())
                        .fg((e.isDir()) ? GREEN : RED).a(" " + e.getFileName().toString())
                        .reset());
        out.flush();
    }

    @Override
    public void close() {
        AnsiConsole.systemUninstall();
    }
}
