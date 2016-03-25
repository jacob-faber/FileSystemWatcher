package com.camabeh.fswatcher.output;

public class WriterFactory {
    public static Writer getWriter(Writer.Format format) {
        switch (format) {
            case CSV:
                return new CsvWriter();
            case XML:
                return new XmlWriter();
            case JSON:
                return new JsonWriter();
            case PRETTY:
                return new PrettyWriter();
            default:
                return null;
        }
    }
}
