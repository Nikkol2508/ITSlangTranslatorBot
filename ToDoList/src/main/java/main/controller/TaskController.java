package main.controller;

import main.model.Task;
import main.service.TaskService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/task/")
    public int add(@RequestParam String name, @RequestParam String description) {
        Task task = new Task();
        task.setName(name);
        task.setDescription(description);
        return taskService.addTask(task);
    }

    @GetMapping("/task/")
     public List<Task> list(){
        return taskService.getListTasks();
    }

    @GetMapping("/task/{id}")
    public Task get(@PathVariable int id) throws EntityNotFoundException {
        return taskService.getById(id);
    }

    @DeleteMapping("/task/")
    public void clearTasks() {
        taskService.deleteAllTasks();
    }


    @PutMapping("/task/{id}")
    public int changTask(@PathVariable int id, @RequestParam String name, @RequestParam String description) throws EntityNotFoundException {
        Task task = taskService.getById(id);
        Task newTask = task;
        newTask.setName(name);
        newTask.setDescription(description);
        taskService.addTask(newTask);
        return id;
    }

    @DeleteMapping("/task/{id}")
    public int deleteTask(@PathVariable int id) throws EntityNotFoundException {
        //Task task = taskService.getById(id);
        taskService.deleteTask(id);
        return id;
    }



}
