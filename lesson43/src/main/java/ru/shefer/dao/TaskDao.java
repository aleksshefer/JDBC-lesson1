package ru.shefer.dao;

import ru.shefer.entity.Task;

import java.util.List;

public interface TaskDao {
    Task save(Task task);

    List<Task> findAll();

    int deleteAll();

    Task getById(Integer id);

    List<Task> findAllNotFinished();

    List<Task> findNewestTasks(Integer numberOfNewestTasks);

    Task finishTask(Task task);

    void deleteById(Integer id);
}
