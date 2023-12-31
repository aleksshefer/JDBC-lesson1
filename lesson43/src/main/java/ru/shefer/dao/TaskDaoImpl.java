package ru.shefer.dao;

import ru.shefer.entity.Task;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

@Component
public class TaskDaoImpl implements TaskDao {
    private final DataSource dataSource;

    public TaskDaoImpl(DataSource dataSource) {
        System.out.println(2222);
        this.dataSource = dataSource;
    }

    @Override
    public Task save(Task task) {

        String sql = "INSERT INTO task (title, finished, created_date) VALUES (?, ?, ?)";
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {

            statement.setString(1, task.getTitle());
            statement.setBoolean(2, task.getFinished());
            statement.setTimestamp(3, java.sql.Timestamp.valueOf(task.getCreatedDate()));
            statement.executeUpdate();

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    task.setId(resultSet.getInt(1));
                }
            }

        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }

        return task;
    }

    @Override
    public List<Task> findAll() {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT task_id, title, finished, created_date FROM task ORDER BY task_id";
        findAllBySQL(tasks, sql);

        return tasks;
    }

    @Override
    public int deleteAll() {

        String sql = "WITH deleted AS (DELETE FROM task RETURNING *) SELECT COUNT(*) FROM deleted";
        try (
                Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)) {

            if (resultSet.next())
                return resultSet.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }

    @Override
    public Task getById(Integer id) {
        Task resultTask = null;
        String sql = "SELECT * FROM task WHERE task_id = ?";

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                resultTask = new Task(
                        resultSet.getString(2),
                        resultSet.getBoolean(3),
                        resultSet.getTimestamp(4).toLocalDateTime()
                );
                resultTask.setId(resultSet.getInt(1));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultTask;
    }

    @Override
    public List<Task> findAllNotFinished() {

        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT task_id, title, finished, created_date FROM task WHERE finished = false ORDER BY task_id";
        findAllBySQL(tasks, sql);
        return tasks;
    }

    @Override
    public List<Task> findNewestTasks(Integer numberOfNewestTasks) {

        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT task_id, title, finished, created_date FROM task ORDER BY task_id DESC LIMIT ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setInt(1, numberOfNewestTasks);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Task task = new Task(
                            resultSet.getString(2),
                            resultSet.getBoolean(3),
                            resultSet.getTimestamp(4).toLocalDateTime()
                    );
                    task.setId(resultSet.getInt(1));
                    tasks.add(task);
                }
            }
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
        return tasks;
    }

    @Override
    public Task finishTask(Task task) {

        String sql = "UPDATE task SET finished = true WHERE task_id = ?";

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setInt(1, task.getId());
            preparedStatement.executeUpdate();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return getById(task.getId());
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM task WHERE task_id = ?";
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void findAllBySQL(List<Task> tasks, String sql) {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)
        ) {

            while (resultSet.next()) {
                Task task = new Task(
                        resultSet.getString(2),
                        resultSet.getBoolean(3),
                        resultSet.getTimestamp(4).toLocalDateTime()
                );
                task.setId(resultSet.getInt(1));
                tasks.add(task);
            }

        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
    }
}
