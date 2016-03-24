package com.camabeh;

import org.apache.commons.cli.*;

import java.nio.file.Path;
import java.nio.file.WatchEvent;

import static java.nio.file.StandardWatchEventKinds.*;

public class App extends CommandLine {
    public static void main(String[] args) throws Exception {
        Options opts = new Options()
                .addOption("p", "path", true, "Path")
                .addOption("f", "format", true, "[pretty || csv || xml || json]")
                .addOption("e", "events", true, "[cdm] create/delete/modify ")
                .addOption("v", "verbose", false, "Enable debbuging info")
                .addOption("r", "recursive", false, "Enable debbuging info")
                .addOption("h", "help", false, "Print this help");

        CommandLineParser parser = new DefaultParser();

        String path = ".";
        boolean verbose = false;
        boolean recursive = false;
        Writer.Format format = Writer.Format.PRETTY;
        WatchEvent.Kind<Path>[] events = new WatchEvent.Kind[]{ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY};

        try {
            CommandLine line = parser.parse(opts, args);
            if (line.hasOption("h")) {
                new HelpFormatter().printHelp("fs-watcher", opts);
                System.exit(0);
            }
            if (line.hasOption("v")) {
                verbose = true;
            }
            if (line.hasOption("r")) {
                recursive = true;
            }
            if (line.hasOption("p")) {
                path = line.getOptionValue("p");
            }
            if (line.hasOption("e")) {
                events = Event.parse(line.getOptionValue("e"));
            }
            if (line.hasOption("f")) {
                format = Writer.parse(line.getOptionValue("f")); //Formatter.parse(line.getOptionValue("f"))
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        FileWatcherService watcher = new FileWatcherService(format, events, verbose, recursive,
                path);
        watcher.setListener(new FileWatcherServiceListener() {
            @Override
            public void onCreate(Event e) {
                System.out.println("onCreate " + e);
            }

            @Override
            public void onDelete(Event e) {
                System.out.println("onDelete " + e);
            }

            @Override
            public void onModify(Event e) {
                System.out.println("onModify " + e);
            }
        });
        watcher.start();


        // TODO Ctrl + c cancels watcher
        watcher.stop();

    }
}
