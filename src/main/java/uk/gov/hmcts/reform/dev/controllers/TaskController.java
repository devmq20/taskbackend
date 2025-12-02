package uk.gov.hmcts.reform.dev.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.reform.dev.models.Task;
import uk.gov.hmcts.reform.dev.repository.TaskRepository;


import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskRepository taskRepository;

    //@CrossOrigin(origins = "http://localhost:4200")
    @CrossOrigin(origins = "*")
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Task> createTask(@Valid @RequestBody Task task) {

        // id is auto-generated, ignore incoming id if any
        task.setId(null);

        Task saved = taskRepository.save(task);

        return ResponseEntity.ok(saved);
    }


    //@CrossOrigin(origins = "http://localhost:4200")
    @CrossOrigin(origins = "*")
    @GetMapping(produces = "application/json")
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return ResponseEntity.ok(tasks);
    }
}
