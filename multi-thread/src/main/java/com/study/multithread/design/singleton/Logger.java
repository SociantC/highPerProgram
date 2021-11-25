package com.study.multithread.design.singleton;

import sun.rmi.runtime.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {
    private FileWriter writer;
    private static final Logger instance = new Logger();

    public Logger() {
        File file = new File("/user/log.txt");
        try {
            writer = new FileWriter(file, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Logger getInstance() {
        return instance;
    }

    public void log(String message) throws IOException {
        writer.write(message);
    }
}
