package com.antigravity.ghosttask.infrastructure.config;

import com.antigravity.ghosttask.application.ReminderService;
import com.antigravity.ghosttask.domain.ReminderRepository;
import com.antigravity.ghosttask.infrastructure.MarkdownReminderRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ReminderRepository reminderRepository() {
        // Using a fixed path for now, can be externalized to properties later
        return new MarkdownReminderRepository("./data/reminders");
    }

    @Bean
    public ReminderService reminderService(ReminderRepository reminderRepository) {
        return new ReminderService(reminderRepository);
    }
}
