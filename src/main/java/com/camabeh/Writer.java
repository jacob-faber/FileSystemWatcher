package com.camabeh;

import java.io.BufferedOutputStream;
import java.io.PrintStream;

public abstract class Writer {

    protected PrintStream out;

    enum Format {
        CSV,
        PRETTY,
        JSON,
        XML
    }

    public Writer(PrintStream out) {
        this.out = out;
    }

    public Writer() {
        out = new PrintStream(new BufferedOutputStream(System.out));
    }

    abstract void write(Event event);

    public static Format parse(String formatType) {
        switch (formatType) {
            case "pretty":
                return Format.PRETTY;
            case "csv":
                return Format.CSV;
            case "json":
                return Format.JSON;
            case "xml":
                return Format.XML;
            default:
                throw new IllegalArgumentException("Invalid format, possible values: pretty|csv|json|xml");
        }
    }
}
