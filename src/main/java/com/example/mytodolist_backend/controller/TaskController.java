package com.example.mytodolist_backend.controller;

import com.example.mytodolist_backend.HelloController;
import com.example.mytodolist_backend.dto.TaskDto;
import com.example.mytodolist_backend.entity.Task;
import com.example.mytodolist_backend.repository.TaskRepository;
import com.example.mytodolist_backend.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private static final Logger log = LoggerFactory.getLogger(HelloController.class);

    private final TaskRepository taskRepository;

    private final TaskService taskService;


    public TaskController(TaskRepository taskRepository, TaskService taskService) {
        this.taskService = taskService;

        Assert.notNull(taskRepository, "TaskRepositoryはNULLが許可されていません");
        this.taskRepository = taskRepository;
    }


    @GetMapping("/getAll")
    public List<Task> getAll(){
        return taskRepository.findAll();
    }


    @PostMapping("/createTask")
    public ResponseEntity<Task> createTask(@RequestBody TaskDto taskDto){
        try {
            Task newTask = taskService.createNewTask(taskDto);
            return new ResponseEntity<>(newTask, HttpStatus.CREATED);
        } catch (DataAccessException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "データベースにアクセスできませんでした", e);
        } catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "不正な値です", e);
        }
    }


    @GetMapping("/get")
    public ResponseEntity<?> getSomething(@RequestParam Long id){
        Task task = taskRepository.findById(id).orElse(null);
        if (task == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            log.info("Task found: {}", task); //タスクの内容をログに出力
            return ResponseEntity.ok(task);
        }
    }



    @GetMapping("/getByStatus")
    public ResponseEntity<?> getByStatus(@RequestParam Long id){
        List<Task> tasks = taskRepository.findByStatusId(id);
        if (tasks == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            log.info("Task found: {}", tasks);
            return ResponseEntity.ok(tasks);
        }
    }


    @GetMapping("/getByYearAndMonth")
    public ResponseEntity<?> getByYearAndMonth(@RequestParam Integer year, @RequestParam Integer month){
        List<Task> tasks = taskRepository.findByYearAndMonth(year, month);
        if (tasks == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            log.info("Task found: {}", tasks);
            return ResponseEntity.ok(tasks);
        }
    }

}
