package com.nhat.biotech.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.logging.Logger;

public abstract class DataGenerator {

    private static final Logger LOGGER = Logger.getLogger(DataGenerator.class.getName());

    protected static final String GEN_DATA_PATH = "src/generated/resources/data/biotech";
    protected static final String GEN_ASSETS_PATH = "src/generated/resources/assets/biotech";
    protected static final String ASSETS_PATH = "src/main/resources/assets/biotech";

    protected static void clearDirectory(String path) {
        try (var paths = Files.walk(Paths.get(path))) {
            paths.sorted(Comparator.reverseOrder())
                    .filter(p -> !p.equals(Paths.get(path))) // Skip the root directory
                    .forEach(p -> {
                        try {
                            Files.delete(p);
                        } catch (IOException e) {
                            LOGGER.warning("Failed to delete " + p);
                        }
                    });
        } catch (IOException e) {
            LOGGER.warning("Failed to clear directory " + path);
            e.printStackTrace();
        }
    }

    protected abstract void generate();

    public static void main(String[] args) {

        RecipeGenerator recipeGenerator = new RecipeGenerator();
        recipeGenerator.generate();

        MachineGenerator machineGenerator = new MachineGenerator();
        machineGenerator.generate();
    }
}
