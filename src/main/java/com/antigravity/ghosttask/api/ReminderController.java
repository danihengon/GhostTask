package com.antigravity.ghosttask.api;

import com.antigravity.ghosttask.api.dto.CreateReminderRequest;
import com.antigravity.ghosttask.api.dto.ReminderResponse;
import com.antigravity.ghosttask.application.ReminderService;
import com.antigravity.ghosttask.domain.Reminder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reminders")
public class ReminderController {

    private final ReminderService reminderService;

    public ReminderController(ReminderService reminderService) {
        this.reminderService = reminderService;
    }

    @PostMapping
    public ResponseEntity<ReminderResponse> createReminder(@RequestBody CreateReminderRequest request) {
        Reminder created = reminderService.createReminder(
                request.getTitle(),
                request.getDescription(),
                request.getLatitude(),
                request.getLongitude(),
                request.getRadiusMeters(),
                request.isNotifyOnlyOnce());
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(created));
    }

    @GetMapping
    public List<ReminderResponse> getAllReminders() {
        return reminderService.getAllReminders().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ReminderResponse getReminderById(@PathVariable UUID id) {
        return toResponse(reminderService.getReminderById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReminder(@PathVariable UUID id) {
        reminderService.deleteReminder(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<Void> completeReminder(@PathVariable UUID id) {
        reminderService.completeReminder(id);
        return ResponseEntity.ok().build();
    }

    // --- Helpers ---

    private ReminderResponse toResponse(Reminder reminder) {
        ReminderResponse response = new ReminderResponse();
        response.setId(reminder.getId());
        response.setTitle(reminder.getTitle());
        response.setDescription(reminder.getDescription());
        response.setCreatedAt(reminder.getCreatedAt());
        response.setCompleted(reminder.isCompleted());
        response.setLatitude(reminder.getLatitude());
        response.setLongitude(reminder.getLongitude());
        response.setRadiusMeters(reminder.getRadiusMeters());
        response.setNotifyOnlyOnce(reminder.isNotifyOnlyOnce());
        return response;
    }

    // --- Exception Handlers ---

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleBadRequest() {
        // Return 400
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleNotFound() {
        // Return 404
    }
}
