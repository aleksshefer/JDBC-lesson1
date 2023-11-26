package ru.shefer.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Task {
    private Integer id;
    private String title;
    private Boolean finished;
    private LocalDateTime createdDate;

    public Task(String title, Boolean finished, LocalDateTime createdDate) {
        this.title = title;
        this.finished = finished;
        this.createdDate = createdDate;
    }
}
