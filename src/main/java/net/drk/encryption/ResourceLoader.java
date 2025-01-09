package net.drk.encryption;

import net.drk.DarkMod;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ResourceLoader implements SimpleSynchronousResourceReloadListener {

    private static final Identifier ID = Identifier.of(DarkMod.MOD_ID, "resource_reload_listener");
    private Set<Identifier> loadedResources = new HashSet<>(); // Store loaded resource identifiers

    @Override
    public Identifier getFabricId() {
        return ID;
    }

    @Override
    public void reload(ResourceManager manager) {
        String[] folders = {"textures/item", "models/item"};

        for (String folderPath : folders) {
            Map<Identifier, List<Resource>> resources = manager.findAllResources(folderPath,
                    id -> id.getNamespace().equals(DarkMod.MOD_ID) &&
                            (id.getPath().endsWith(".png") || id.getPath().endsWith(".json")));

            // Log the number of resources found in this folder
            System.out.println("Searching in folder: " + folderPath);
            System.out.println("Resources found: " + resources.size());

            for (Map.Entry<Identifier, List<Resource>> entry : resources.entrySet()) {
                Identifier resourceId = entry.getKey();

                if (!loadedResources.contains(resourceId)) {
                    try {
                        for (Resource resource : entry.getValue()) {
                            resource.getInputStream().close(); // Access the resource to force load it
                            System.out.println("Loaded new resource: " + resourceId);
                        }
                        loadedResources.add(resourceId); // Track the loaded resource
                    } catch (IOException e) {
                        System.err.println("Failed to load resource: " + resourceId);
                        e.printStackTrace();
                    }
                }
            }
        }

        System.out.println("Total loaded resources: " + loadedResources.size());
    }
}