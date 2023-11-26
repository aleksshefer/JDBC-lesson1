package ru.shefer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.shefer.dao.TaskDao;
import ru.shefer.entity.Task;

@Controller
@RequiredArgsConstructor
public class FinishController {
    private final TaskDao taskDao;

    @RequestMapping("/finish")
    public String finishTask(@RequestParam("taskId") int taskId) {
        Task taskToFinish = taskDao.getById(taskId);
        taskDao.finishTask(taskToFinish);
        return "redirect:/";
    }
}
