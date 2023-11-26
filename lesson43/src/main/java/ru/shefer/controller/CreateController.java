package ru.shefer.controller;

import ru.shefer.dao.TaskDao;
import ru.shefer.entity.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Controller
public class CreateController {
    private final TaskDao taskDao;

    @GetMapping("/create-form")
    public String createForm() {
        return "create-form";
    }

    @PostMapping("/create")
    public String createTask(@RequestParam("title") String title) {
        taskDao.save(new Task(title, false, LocalDateTime.now()));
        return "redirect:/";
    }
}
