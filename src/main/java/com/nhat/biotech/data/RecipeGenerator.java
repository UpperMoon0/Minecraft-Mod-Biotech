package com.nhat.biotech.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nhat.biotech.Biotech;
import com.nhat.biotech.data.models.Creature;
import com.nhat.biotech.data.models.Drop;
import com.nhat.biotech.data.models.Food;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;

public class RecipeGenerator extends DataGenerator {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Logger LOGGER = Logger.getLogger(RecipeGenerator.class.getName());

    @Override
    public void generate() {
        generateBreedingChamberRecipes();
        generateTerrestrialHabitatRecipes();
        generateSlaughterhouseRecipes();
    }

    public void generateBreedingChamberRecipes() {
        String machineId = "breeding_chamber";
        String type = Biotech.MOD_ID + ":" + machineId;

        List<Creature> creatures = CreatureData.CREATURES;

        for (Creature creature : creatures) {
            List<Food> foods = CreatureData.FOODS.get(creature);

            for (Food food : foods) {
                ItemJson[] itemInputs = new ItemJson[]{
                        new ItemJson(creature.id(), 2),
                        new ItemJson(food.id(), 2)
                };
                boolean[] itemConsumed = new boolean[]{
                        false,
                        true
                };
                FluidJson[] fluidInputs = new FluidJson[]{
                        new FluidJson(CreatureData.FLUID_WATER, 200)
                };
                ItemJson[] itemOutputs = new ItemJson[]{
                        new ItemJson(creature.babyId(), 1)
                };
                FluidJson[] fluidOutputs = new FluidJson[]{};
                int energy = 20000;

                String creatureName = creature.id().substring(creature.id().indexOf(":") + 1);
                String foodName = food.id().substring(food.id().indexOf(":") + 1);
                String recipeName = machineId + "_" + creatureName + "_t" + food.tier() + "_" + foodName;

                RecipeJson recipeJson = new RecipeJson(type, itemInputs, itemConsumed, itemOutputs, fluidInputs, fluidOutputs, energy);

                generateRecipe(recipeName, recipeJson);
            }
        }
    }

    public void generateTerrestrialHabitatRecipes() {
        String machineId = "terrestrial_habitat";
        String type = Biotech.MOD_ID + ":" + machineId;

        List<Creature> creatures = CreatureData.CREATURES;

        for (Creature creature : creatures) {
            List<Food> foods = CreatureData.FOODS.get(creature);

            for (Food food : foods) {
                ItemJson[] itemInputs = new ItemJson[]{
                        new ItemJson(creature.babyId(), 1),
                        new ItemJson(food.id(), 4)
                };
                boolean[] itemConsumed = new boolean[]{
                        true,
                        true
                };
                FluidJson[] fluidInputs = new FluidJson[]{
                        new FluidJson(CreatureData.FLUID_WATER, 500)
                };
                ItemJson[] itemOutputs = new ItemJson[]{
                        new ItemJson(creature.id(), 1)
                };
                FluidJson[] fluidOutputs = new FluidJson[]{};
                int energy = 48000;

                String creatureName = creature.id().substring(creature.id().indexOf(":") + 1);
                String foodName = food.id().substring(food.id().indexOf(":") + 1);
                String recipeName = machineId + "_" + creatureName + "_t" + food.tier() + "_" + foodName;

                RecipeJson recipeJson = new RecipeJson(type, itemInputs, itemConsumed, itemOutputs, fluidInputs, fluidOutputs, energy);

                generateRecipe(recipeName, recipeJson);
            }
        }
    }

    public void generateSlaughterhouseRecipes() {
        String machineId = "slaughterhouse";
        String type = Biotech.MOD_ID + ":" + machineId;

        List<Creature> creatures = CreatureData.CREATURES;

        for (Creature creature : creatures) {
            List<Drop> drops = CreatureData.DROPS.get(creature);

            for (Drop drop : drops) {
                ItemJson[] itemInputs = new ItemJson[]{
                        new ItemJson(creature.id(), 1)
                };
                boolean[] itemConsumed = new boolean[]{
                        true
                };
                FluidJson[] fluidInputs = new FluidJson[]{
                        new FluidJson(CreatureData.FLUID_WATER, 200)
                };
                ItemJson[] itemOutputs = CreatureData.DROPS.get(creature).stream()
                        .map(d -> new ItemJson(d.id(), d.count()))
                        .toArray(ItemJson[]::new);
                FluidJson[] fluidOutputs = new FluidJson[]{};
                int energy = 16000;

                String creatureName = creature.id().substring(creature.id().indexOf(":") + 1);
                String recipeName = machineId + "_" + creatureName;

                RecipeJson recipeJson = new RecipeJson(type, itemInputs, itemConsumed, itemOutputs, fluidInputs, fluidOutputs, energy);

                generateRecipe(recipeName, recipeJson);
            }
        }
    }

    private void generateRecipe(String recipeName, RecipeJson recipeJson) {
        String json = GSON.toJson(recipeJson);

        Path outputPath = Paths.get(GEN_RECIPES_PATH, recipeName + ".json");
        try {
            Files.createDirectories(outputPath.getParent());
            LOGGER.info("Generating recipe " + recipeName);
            try (FileWriter writer = new FileWriter(outputPath.toFile())) {
                writer.write(json);
            }
        } catch (IOException e) {
            LOGGER.severe("Failed to write recipe to file: " + outputPath);
            e.printStackTrace();
        }
    }

    private static class RecipeJson {
        private final String type;
        private final ItemJson[] itemInputs;
        private final boolean[] itemConsumed;
        private final ItemJson[] itemOutputs;
        private final FluidJson[] fluidInputs;
        private final FluidJson[] fluidOutputs;
        private final int energy;

        public RecipeJson(String type,
                          ItemJson[] itemInputs,
                          boolean[] itemConsumed,
                          ItemJson[] itemOutputs,
                          FluidJson[] fluidInputs,
                          FluidJson[] fluidOutputs,
                          int energy) {
            this.type = type;
            this.itemInputs = itemInputs;
            this.itemConsumed = itemConsumed;
            this.itemOutputs = itemOutputs;
            this.fluidInputs = fluidInputs;
            this.fluidOutputs = fluidOutputs;
            this.energy = energy;
        }
    }

    private static class ItemJson {
        private final String id;
        private final int Count;

        public ItemJson(String id, int count) {
            this.id = id;
            this.Count = count;
        }
    }

    private static class FluidJson {
        private final String FluidName;
        private final int Amount;

        public FluidJson(String fluidName, int amount) {
            this.FluidName = fluidName;
            this.Amount = amount;
        }
    }
}
