package com.camabeh;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.nio.file.StandardWatchEventKinds.*;

public class FileWatcherService implements Service {

    private static final Logger log = Logger.getLogger(Service.class.getName());

    private Path[] paths = new Path[]{Paths.get(".")};
    private Writer.Format format = Writer.Format.PRETTY;
    private WatchEvent.Kind<?>[] events = new WatchEvent.Kind[]{ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE};
    private FileWatcherServiceListener listener = new FileWatcherServiceListener() {
    };

    private boolean recursive = false;

    private final WatchService watcher;
    private final Map<WatchKey, Path> keys;

    public FileWatcherService(Writer.Format format, WatchEvent.Kind<?>[] events, boolean recursive, String... paths) throws IOException {
        this.format = format;
        this.events = events;
        this.recursive = recursive;

        if (paths != null) {
            this.paths = new Path[paths.length];
        } // Else using default value

        this.watcher = FileSystems.getDefault().newWatchService();
        this.keys = new HashMap<>();

        log.log(Level.FINE, "onCreate constructor  {0}", "1");
        log.log(Level.FINER, "onCreate constructor  {0}", "2");
        log.log(Level.FINEST, "onCreate constructor");
    }

    /**
     * Register the given directory with the WatchService
     */
    public void register(final Path dir) throws IOException {
        WatchKey key = dir.register(watcher, events);
        if (log.isLoggable(Level.FINER)) {
            Path prev = keys.get(key);
            if (prev == null) {
                log.log(Level.FINER, "Register: %s", dir);
            } else {
                if (!dir.equals(prev)) {
                    log.log(Level.FINER, "Updated %s -> %s", dir);
                }
            }
        }
        keys.put(key, dir);
    }

    /**
     * Register the given directory, and all its sub-directories with the
     * WatchService
     */
    public void registerAll(final Path startDir) throws IOException {
        Files.walkFileTree(startDir, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                register(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    @Override
    public void start() throws IOException {
        if (recursive) {
            log.log(Level.FINE, "Recursively scanning to register all folders");
            for (Path path : paths) {
                registerAll(path);
            }
        } else {
            log.log(Level.FINE, "Scanning folders and registering them");
            for (Path path : paths) {
                register(path);
            }
        }

        while (true) {
            WatchKey key;

            try {
                key = watcher.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }

            Path dir = keys.get(key);
            if (dir == null) {
                log.log(Level.SEVERE, "WatchKey not recognized!");
                continue;
            }

            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind kind = event.kind();

                if (kind == ENTRY_CREATE) {
                    this.listener.onCreate(new Event());
                }
                if (kind == ENTRY_DELETE) {
                    this.listener.onDelete(new Event());
                }
                if (kind == ENTRY_MODIFY) {
                    this.listener.onModify(new Event());
                }
            }
        }
    }

    @Override
    public void stop() throws IOException {
        // TODO stop
    }

    public void setListener(FileWatcherServiceListener listener) {
        this.listener = listener;
    }

    public FileWatcherServiceListener getListener() {
        return listener;
    }
}


