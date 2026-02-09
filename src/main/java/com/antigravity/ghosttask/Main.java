package com.antigravity.ghosttask;

import com.antigravity.ghosttask.application.ReminderService;
import com.antigravity.ghosttask.domain.Reminder;
import com.antigravity.ghosttask.infrastructure.MarkdownReminderRepository;

import java.io.File;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Ghost Task Manual Test ===");

        // 1. Setup Infrastructure
        // Use a relative path for testing, handled by the repository logic
        String storagePath = "data/reminders";
        System.out.println("Initializing repository at: " + new File(storagePath).getAbsolutePath());

        MarkdownReminderRepository repository = new MarkdownReminderRepository(storagePath);

        // 2. Setup Application
        ReminderService service = new ReminderService(repository);

        // 3. Execute Scenario
        System.out.println("\n--> Creating new reminder...");
        try {
            Reminder created = service.createReminder(
                    "Buy Milk",
                    "Don't forget the semi-skimmed milk",
                    40.4168,
                    -3.7038,
                    100.0,
                    true);
            System.out.println("SUCCESS: Created reminder with ID: " + created.getId());
        } catch (Exception e) {
            System.err.println("FAILED to create reminder: " + e.getMessage());
            e.printStackTrace();
        }

        // 4. Verify Persistence
        System.out.println("\n--> Listing all reminders...");
        List<Reminder> allReminders = service.getAllReminders();
        System.out.println("Found " + allReminders.size() + " reminders:");

        for (Reminder r : allReminders) {
            System.out.printf(" - [%s] %s (Completed: %s)%n",
                    r.getId(), r.getTitle(), r.isCompleted());
        }

        System.out.println("\n=== Test Complete ===");
    }
}
