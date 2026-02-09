package com.antigravity.ghosttask.infrastructure;

import com.antigravity.ghosttask.domain.Reminder;
import com.antigravity.ghosttask.domain.ReminderRepository;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MarkdownReminderRepository implements ReminderRepository {

    private final Path storagePath;

    public MarkdownReminderRepository(String storageDir) {
        this.storagePath = Paths.get(storageDir);
        initStorage();
    }

    private void initStorage() {
        try {
            if (!Files.exists(storagePath)) {
                Files.createDirectories(storagePath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage directory", e);
        }
    }

    @Override
    public void save(Reminder reminder) {
        String content = serialize(reminder);
        Path file = storagePath.resolve(reminder.getId().toString() + ".md");
        try {
            Files.write(file, content.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Failed to save reminder: " + reminder.getId(), e);
        }
    }

    @Override
    public Optional<Reminder> findById(UUID id) {
        Path file = storagePath.resolve(id.toString() + ".md");
        if (!Files.exists(file)) {
            return Optional.empty();
        }
        try {
            String content = new String(Files.readAllBytes(file));
            return Optional.of(deserialize(content));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read reminder: " + id, e);
        }
    }

    @Override
    public List<Reminder> findAll() {
        try (Stream<Path> paths = Files.walk(storagePath)) {
            return paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".md"))
                    .map(path -> {
                        try {
                            String content = new String(Files.readAllBytes(path));
                            return deserialize(content);
                        } catch (IOException e) {
                            System.err.println("Failed to parse file: " + path);
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Failed to list reminders", e);
        }
    }

    @Override
    public void delete(UUID id) {
        Path file = storagePath.resolve(id.toString() + ".md");
        try {
            Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete reminder: " + id, e);
        }
    }

    // Custom serialization logic (Standard Java)

    private String serialize(Reminder reminder) {
        StringBuilder sb = new StringBuilder();
        sb.append("---\n");
        sb.append("id: ").append(reminder.getId()).append("\n");
        sb.append("title: ").append(reminder.getTitle()).append("\n");
        sb.append("createdAt: ").append(reminder.getCreatedAt()).append("\n");
        sb.append("completed: ").append(reminder.isCompleted()).append("\n");
        sb.append("latitude: ").append(reminder.getLatitude()).append("\n");
        sb.append("longitude: ").append(reminder.getLongitude()).append("\n");
        sb.append("radiusMeters: ").append(reminder.getRadiusMeters()).append("\n");
        sb.append("notifyOnlyOnce: ").append(reminder.isNotifyOnlyOnce()).append("\n");
        if (reminder.getLastTriggeredAt() != null) {
            sb.append("lastTriggeredAt: ").append(reminder.getLastTriggeredAt()).append("\n");
        }
        sb.append("timesTriggered: ").append(reminder.getTimesTriggered()).append("\n");
        sb.append("---\n");
        if (reminder.getDescription() != null) {
            sb.append(reminder.getDescription());
        }
        return sb.toString();
    }

    private Reminder deserialize(String fileContent) {
        // Very basic parsing assuming predictable format
        Map<String, String> yaml = new HashMap<>();
        String description = "";

        String[] parts = fileContent.split("---", 3);
        if (parts.length >= 2) {
            String frontMatter = parts[1];
            if (parts.length > 2) {
                description = parts[2].trim();
            }

            Scanner scanner = new Scanner(frontMatter);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty())
                    continue;
                String[] keyValue = line.split(":", 2);
                if (keyValue.length == 2) {
                    yaml.put(keyValue[0].trim(), keyValue[1].trim());
                }
            }
            scanner.close();
        }

        // Parse fields
        String title = yaml.getOrDefault("title", "Untitled");
        double lat = Double.parseDouble(yaml.getOrDefault("latitude", "0.0"));
        double lon = Double.parseDouble(yaml.getOrDefault("longitude", "0.0"));
        double radius = Double.parseDouble(yaml.getOrDefault("radiusMeters", "0.0"));
        boolean notifyOnce = Boolean.parseBoolean(yaml.getOrDefault("notifyOnlyOnce", "false"));

        // Reconstruct Reminder
        // Note: The constructor generates a new ID/CreatedAt, so we need to bypass or
        // use a comprehensive constructor.
        // Since the current Reminder class only has a 'creating' constructor, we might
        // need to add setters or a full constructor.
        // For now, I will use setters to restore state as Reminder class allows mutable
        // state via setters for some fields,
        // but ID and CreatedAt are final.
        // I should probably REFLECT or modify the domain class to allow reconstruction.
        // Given I just wrote the domain class, I should verify if I can set
        // ID/CreatedAt.
        // Checking Reminder.java... ID and CreatedAt are final and no setters.
        // I need to modifying Reminder.java to add a reconstruction constructor or
        // remove final.

        // Let's assume I need to update Reminder.java or use reflection.
        // Cleanest is to add a constructor for reconstitution.

        return reconstructReminder(yaml, description);
    }

    // Helper using reflection to set final fields if we don't want to pollute the
    // public API
    // Or better, add a protected/package-private constructor for hydration.
    // For this task, I'll assume I can modify Reminder.java or use reflection.
    // Reflection is standard in frameworks.

    private Reminder reconstructReminder(Map<String, String> yaml, String description) {
        try {
            // Create a dummy instance using public constructor
            Reminder r = new Reminder(
                    yaml.get("title"),
                    description,
                    Double.parseDouble(yaml.get("latitude")),
                    Double.parseDouble(yaml.get("longitude")),
                    Double.parseDouble(yaml.get("radiusMeters")),
                    Boolean.parseBoolean(yaml.get("notifyOnlyOnce")));

            // Now override fields using reflection to match persisted state
            setField(r, "id", UUID.fromString(yaml.get("id")));
            setField(r, "createdAt", LocalDateTime.parse(yaml.get("createdAt")));
            setField(r, "completed", Boolean.parseBoolean(yaml.get("completed")));

            if (yaml.containsKey("lastTriggeredAt")) {
                setField(r, "lastTriggeredAt", LocalDateTime.parse(yaml.get("lastTriggeredAt")));
            }
            if (yaml.containsKey("timesTriggered")) {
                setField(r, "timesTriggered", Integer.parseInt(yaml.get("timesTriggered")));
            }

            return r;
        } catch (Exception e) {
            throw new RuntimeException("Error reconstituting reminder", e);
        }
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        java.lang.reflect.Field f = target.getClass().getDeclaredField(fieldName);
        f.setAccessible(true);
        f.set(target, value);
    }
}
