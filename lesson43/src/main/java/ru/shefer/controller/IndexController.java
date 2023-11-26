package ru.shefer.controller;

import ru.shefer.dao.TaskDao;
import ru.shefer.entity.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


import java.util.List;

@RequiredArgsConstructor
@Controller
public class IndexController {
    private final TaskDao taskDao;

    @GetMapping("/")
    public String index(Model model) {
        List<Task> tasks = taskDao.findAll();
        model.addAttribute("tasks", tasks);
        return "index";
    }
}
