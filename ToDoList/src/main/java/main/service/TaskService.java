package main.service;

import main.controller.EntityNotFoundException;
import main.model.Task;
import main.model.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {

    private static TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public int addTask(Task task) {
        taskRepository.save(task);
        return task.getId();
    }

    public List<Task> getListTasks(){
        Iterable<Task> taskIterable = taskRepository.findAll();
        ArrayList<Task> tasks = new ArrayList<>();
        for(Task task : taskIterable) {
            tasks.add(task);
        }
        return tasks;
    }

    public Task getById(int id) throws EntityNotFoundException {
        Task task = taskRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Task not found by id: " + id));
        return task;
    }

    public void deleteAllTasks() {
        taskRepository.deleteAll();
    }

    public void deleteTask(int id) throws EntityNotFoundException {
        getById(id);
        taskRepository.deleteById(id);
    }

}
