package com.antigravity.ghosttask.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReminderRepository {
    void save(Reminder reminder);

    Optional<Reminder> findById(UUID id);

    List<Reminder> findAll();

    void delete(UUID id);
}
