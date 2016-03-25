package com.camabeh.fswatcher;

import com.camabeh.fswatcher.output.PrettyWriter;
import com.camabeh.fswatcher.output.Writer;
import com.camabeh.fswatcher.output.WriterFactory;
import org.apache.commons.cli.*;

import java.nio.file.Path;
import java.nio.file.WatchEvent;

import static java.nio.file.StandardWatchEventKinds.*;

public class App {

    private static String path = ".";
    private static boolean verbose = false;
    private static boolean recursive = false;
    private static WatchEvent.Kind<Path>[] events = new WatchEvent.Kind[]{ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY};
    private static Writer writer = new PrettyWriter();

    public static void main(String[] args) throws Exception {
        Options opts = new Options()
                .addOption("p", "path", true, "Path")
                .addOption("f", "format", true, "[pretty || csv || xml || json]")
                .addOption("e", "events", true, "[cdm] create/delete/modify ")
                .addOption("v", "verbose", false, "Enable debbuging info")
                .addOption("r", "recursive", false, "Enable debbuging info")
                .addOption("h", "help", false, "Print this help");

        CommandLineParser parser = new DefaultParser();

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
                writer = WriterFactory.getWriter(Writer.parse(line.getOptionValue("f"))); //Formatter.parse(line.getOptionValue("f"))
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        FileWatcherService watcher = new FileWatcherService(events, recursive, path);
        watcher.setListener(new FileWatcherServiceListener() {
            @Override
            public void onCreate(Event e) {
                System.out.println("onCreate " + e);
                writer.write(e);
            }

            @Override
            public void onDelete(Event e) {
                System.out.println("onDelete " + e);
                writer.write(e);
            }

            @Override
            public void onModify(Event e) {
                System.out.println("onModify " + e);
                writer.write(e);
            }
        });
        watcher.start();


        // TODO Ctrl + c cancels watcher
        watcher.stop();

    }
}
