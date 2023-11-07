package com.example.mytodolist_backend.service;

import com.example.mytodolist_backend.dto.TaskDto;
import com.example.mytodolist_backend.entity.*;
import com.example.mytodolist_backend.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final StatusRepository statusRepository;
    private final PriorityRepository priorityRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, StatusRepository statusRepository, PriorityRepository priorityRepository, CategoryRepository categoryRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.statusRepository = statusRepository;
        this.priorityRepository = priorityRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    public Task createNewTask(TaskDto taskDto){
        Task task = new Task();
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setDueDate(taskDto.getDueDate());

        Status status = statusRepository.findById(taskDto.getStatus())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Status not found"));

        Priority priority = priorityRepository.findById(taskDto.getPriority())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Priority not found"));

        Category category = categoryRepository.findById(taskDto.getCategory())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        User user = userRepository.findById(taskDto.getUser())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        task.setStatus(status);
        task.setPriority(priority);
        task.setCategory(category);
        task.setUser(user);

        return taskRepository.save(task);
    }
}
