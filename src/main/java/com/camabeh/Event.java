package com.camabeh;

import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;

public class Event {
    private static final String delim = "/";

    @SuppressWarnings("unchecked")
    private static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>) event;
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

}
