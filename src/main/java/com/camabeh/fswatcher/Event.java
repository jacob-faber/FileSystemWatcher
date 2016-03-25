package com.camabeh.fswatcher;

import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;

public class Event {

    private static final String delim = "/";

    private final Timestamp timestamp;
    private final String eventType;
    private final Path fileName;
    private final boolean isDir;

    public Event(String eventType, Path fileName, boolean isDir) {
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.eventType = eventType;
        this.fileName = fileName;
        this.isDir = isDir;
    }

    public static WatchEvent.Kind<Path>[] parse(String eventsStr) {
        String[] eventsStrArr = eventsStr.split(delim);
        List<WatchEvent.Kind<Path>> events = new ArrayList<>();

        for (String s : eventsStrArr) {
            switch (s) {
                case "c":
                    events.add(ENTRY_CREATE);
                    break;
                case "d":
                    events.add(ENTRY_DELETE);
                    break;
                case "m":
                    events.add(ENTRY_DELETE);
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected formatting option: " + s);
            }
        }

        if (events.size() == 0) {
            throw new IllegalArgumentException("Expected Event type");
        }

        return events.toArray(new WatchEvent.Kind[events.size()]);
    }

    public String getEventType() {
        return eventType;
    }

    public Path getFileName() {
        return fileName;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public boolean isDir() {
        return isDir;
    }

    @Override
    public String toString() {
        return "Event{" +
                "timestamp=" + timestamp +
                ", eventType='" + eventType + '\'' +
                ", fileName=" + fileName +
                ", isDir=" + isDir +
                '}';
    }
}
