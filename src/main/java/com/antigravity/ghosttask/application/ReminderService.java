package com.antigravity.ghosttask.application;

import com.antigravity.ghosttask.domain.Reminder;
import com.antigravity.ghosttask.domain.ReminderRepository;

import java.util.List;
import java.util.UUID;

public class ReminderService {

    private final ReminderRepository reminderRepository;

    public ReminderService(ReminderRepository reminderRepository) {
        this.reminderRepository = reminderRepository;
    }

    public Reminder createReminder(String title, String description, double latitude, double longitude,
            double radiusMeters, boolean notifyOnlyOnce) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }

        Reminder reminder = new Reminder(title, description, latitude, longitude, radiusMeters, notifyOnlyOnce);
        reminderRepository.save(reminder);
        return reminder;
    }

    public List<Reminder> getAllReminders() {
        return reminderRepository.findAll();
    }

    public Reminder getReminderById(UUID id) {
        return reminderRepository.findById(id)
                .orElseThrow(() -> new java.util.NoSuchElementException("Reminder not found with id: " + id));
    }

    public void completeReminder(UUID id) {
        Reminder reminder = getReminderById(id);
        reminder.markCompleted();
        reminderRepository.save(reminder);
    }

    public void registerTrigger(UUID id) {
        Reminder reminder = getReminderById(id);

        if (reminder.isNotifyOnlyOnce() && reminder.isCompleted()) {
            // Already completed and one-time only, do not trigger again
            return;
            // Alternatively throw exception if strict: throw new
            // IllegalStateException("Reminder already completed");
            // But returning silently is often safer for triggers.
            // Requirement said "Do not allow registering". Return is safe.
        }

        reminder.registerTrigger();
        reminderRepository.save(reminder);
    }

    public void deleteReminder(UUID id) {
        // Verify existence? efficient implementations might just delete.
        // But to be consistent with "clear exception if not found" for operations on
        // ID:
        // However, repo.delete usually is idempotent or simple.
        // Let's check existence if getting strict, otherwise just delete.
        // Given requirements, "If a Reminder does not exist when searching by id, throw
        // a clear exception."
        // Usually delete doesn't require searching, but let's stick to safe pattern.
        if (reminderRepository.findById(id).isEmpty()) {
            throw new IllegalArgumentException("Reminder not found with id: " + id);
        }
        reminderRepository.delete(id);
    }
}
