package com.nhat.biotech.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class MachineGenerator extends DataGenerator {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create();
    private static final Logger LOGGER = Logger.getLogger(MachineGenerator.class.getName());
    private static final String GEN_BLOCKSTATES_PATH = GEN_ASSETS_PATH + "/blockstates";
    private static final String GEN_BLOCK_MODELS_PATH = GEN_ASSETS_PATH + "/models/block";
    private static final String GEN_ITEM_MODELS_PATH = GEN_ASSETS_PATH + "/models/item";
    private static final String TEXTURE_PATH = ASSETS_PATH + "/textures/block";
    private static final String GEN_TEXTURE_PATH = GEN_ASSETS_PATH + "/textures/block";

    @Override
    public void generate() {
        clearDirectory(GEN_BLOCKSTATES_PATH);
        clearDirectory(GEN_BLOCK_MODELS_PATH);
        clearDirectory(GEN_ITEM_MODELS_PATH);
        clearDirectory(GEN_TEXTURE_PATH);

        List<String> machineIds = List.of(
            "breeding_chamber",
            "terrestrial_habitat",
            "slaughterhouse"
        );

        for (String machineId : machineIds) {
            generateMachine(machineId);
        }
    }

    public void generateMachine(String machineId) {
        generateTexture(machineId);
        generateModels(machineId);
        generateBlockstate(machineId);
    }

    public void generateTexture(String machineId) {
        try {
            LOGGER.info("Generating texture for machine " + machineId);

            // Load the base and overlay images
            BufferedImage baseImage = ImageIO.read(new File(TEXTURE_PATH + "/biotech_machine_casing.png"));
            BufferedImage overlayOffImage = ImageIO.read(new File(TEXTURE_PATH + "/" + machineId + "_off_overlay.png"));
            BufferedImage overlayOnImage = ImageIO.read(new File(TEXTURE_PATH + "/" + machineId + "_on_overlay.png"));

            // Create the combined images
            BufferedImage combinedOffImage = new BufferedImage(baseImage.getWidth(), baseImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
            BufferedImage combinedOnImage = new BufferedImage(baseImage.getWidth(), baseImage.getHeight(), BufferedImage.TYPE_INT_ARGB);

            // Draw the base and overlay images
            Graphics gOff = combinedOffImage.getGraphics();
            gOff.drawImage(baseImage, 0, 0, null);
            gOff.drawImage(overlayOffImage, 0, 0, null);
            gOff.dispose();

            Graphics gOn = combinedOnImage.getGraphics();
            gOn.drawImage(baseImage, 0, 0, null);
            gOn.drawImage(overlayOnImage, 0, 0, null);
            gOn.dispose();

            // Save the combined images
            File outputDir = new File(GEN_TEXTURE_PATH);
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }
            ImageIO.write(combinedOffImage, "png", new File(outputDir, machineId + "_off.png"));
            ImageIO.write(combinedOnImage, "png", new File(outputDir, machineId + "_on.png"));
        } catch (IOException e) {
            LOGGER.severe("Failed to generate texture for machine " + machineId);
        }
    }

    public void generateModels(String machineId) {
        try {
            // Create the "on" model JSON
            Map<String, Object> onModel = Map.of(
                    "parent", "minecraft:block/orientable",
                    "textures", Map.of(
                            "front", "biotech:block/" + machineId + "_on",
                            "side", "biotech:block/biotech_machine_casing",
                            "top", "biotech:block/biotech_machine_casing"
                    )
            );

            // Create the "off" model JSON
            Map<String, Object> offModel = Map.of(
                    "parent", "minecraft:block/orientable",
                    "textures", Map.of(
                            "front", "biotech:block/" + machineId + "_off",
                            "side", "biotech:block/biotech_machine_casing",
                            "top", "biotech:block/biotech_machine_casing"
                    )
            );

            // Create the block item model JSON
            Map<String, Object> itemModel = Map.of(
                    "parent", "biotech:block/" + machineId + "_off"
            );

            // Write the JSON files to the appropriate paths
            File onModelFile = new File(GEN_BLOCK_MODELS_PATH, machineId + "_on.json");
            File offModelFile = new File(GEN_BLOCK_MODELS_PATH, machineId + "_off.json");
            File itemModelFile = new File(GEN_ITEM_MODELS_PATH, machineId + ".json");

            // Ensure directories exist
            onModelFile.getParentFile().mkdirs();
            offModelFile.getParentFile().mkdirs();
            itemModelFile.getParentFile().mkdirs();

            // Write the JSON content to the files, overwriting if they exist
            try (FileWriter onWriter = new FileWriter(onModelFile);
                 FileWriter offWriter = new FileWriter(offModelFile);
                 FileWriter itemWriter = new FileWriter(itemModelFile)) {
                GSON.toJson(onModel, onWriter);
                GSON.toJson(offModel, offWriter);
                GSON.toJson(itemModel, itemWriter);
            }

            LOGGER.info("Generated models for machine " + machineId);
        } catch (IOException e) {
            LOGGER.severe("Failed to generate models for machine " + machineId);
        }
    }

    public void generateBlockstate(String machineId) {
        try {
            // Create the blockstate JSON
            Map<String, Object> blockstate = Map.of(
                    "variants", Map.of(
                            "facing=north,operating=false", Map.of("model", "biotech:block/" + machineId + "_off"),
                            "facing=north,operating=true", Map.of("model", "biotech:block/" + machineId + "_on"),
                            "facing=east,operating=false", Map.of("model", "biotech:block/" + machineId + "_off", "y", 90),
                            "facing=east,operating=true", Map.of("model", "biotech:block/" + machineId + "_on", "y", 90),
                            "facing=south,operating=false", Map.of("model", "biotech:block/" + machineId + "_off", "y", 180),
                            "facing=south,operating=true", Map.of("model", "biotech:block/" + machineId + "_on", "y", 180),
                            "facing=west,operating=false", Map.of("model", "biotech:block/" + machineId + "_off", "y", 270),
                            "facing=west,operating=true", Map.of("model", "biotech:block/" + machineId + "_on", "y", 270)
                    )
            );

            // Write the JSON file to the appropriate path
            File blockstateFile = new File(GEN_BLOCKSTATES_PATH, machineId + ".json");

            // Ensure directory exists
            blockstateFile.getParentFile().mkdirs();

            // Write the JSON content to the file, overwriting if it exists
            try (FileWriter writer = new FileWriter(blockstateFile)) {
                GSON.toJson(blockstate, writer);
            }

            LOGGER.info("Generated blockstate for machine " + machineId);
        } catch (IOException e) {
            LOGGER.severe("Failed to generate blockstate for machine " + machineId);
        }
    }
}
