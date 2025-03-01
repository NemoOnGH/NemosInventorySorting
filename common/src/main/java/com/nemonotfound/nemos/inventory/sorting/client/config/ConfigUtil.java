package com.nemonotfound.nemos.inventory.sorting.client.config;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.nemonotfound.nemos.inventory.sorting.Constants;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

public class ConfigUtil {

    private static final Gson GSON = new Gson();
    private static final String DIRECTORY_PATH = "config/nemos-inventory-sorting/";
    private static final String FILE_PATH = DIRECTORY_PATH + "config.json";
    private static final Type configType = new TypeToken<List<ComponentConfig>>() {}.getType();

    public static void writeConfigs(List<ComponentConfig> config) {
        if (Files.exists(Paths.get(FILE_PATH))) {
            return;
        }

        try {
            Files.createDirectories(Paths.get(DIRECTORY_PATH));
        } catch (Exception e) {
            Constants.LOG.error("An error occurred while creating directories:\n", e);
        }

        try(FileWriter writer = new FileWriter(FILE_PATH)) {
            GSON.toJson(config, writer);
        } catch (Exception e) {
            Constants.LOG.error("An error occurred while writing the config:\n", e);
        }
    }

    public static List<ComponentConfig> readConfigs() {
        try(FileReader reader = new FileReader(FILE_PATH)) {
            return GSON.fromJson(reader, configType);
        } catch (Exception e) {
            Constants.LOG.error("An error occurred while reading the config:\n", e);

            return List.of();
        }
    }

    public static Optional<ComponentConfig> getConfigs(List<ComponentConfig> configsList, String componentName) {
        return configsList.stream()
                .filter(config -> config.componentName().equals(componentName))
                .findFirst();
    }
}
