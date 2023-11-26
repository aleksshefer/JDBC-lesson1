package ru.shefer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.shefer.dao.TaskDao;

@Controller
@RequiredArgsConstructor
public class DeleteController {
    private final TaskDao taskDao;

    @DeleteMapping("/delete")
    public String deleteTask(@RequestParam("taskId") int taskId) {
        taskDao.deleteById(taskId);
        return "redirect:/";
    }
}
