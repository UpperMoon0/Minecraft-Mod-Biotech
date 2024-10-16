package com.nhat.biotech.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;

public abstract class DataGenerator {

    protected static final String GEN_DATA_PATH = "src/generated/resources/data/biotech";

    protected static void clearDirectory(String path) {
        try (var paths = Files.walk(Paths.get(path))) {
            paths.sorted(Comparator.reverseOrder()) // Sort in reverse order to delete files before directories
                    .forEach(p -> {
                        try {
                            Files.delete(p);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected abstract void generate();
}
