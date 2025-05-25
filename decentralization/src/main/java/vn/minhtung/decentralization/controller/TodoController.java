package vn.minhtung.decentralization.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.minhtung.decentralization.entity.Todo;
import vn.minhtung.decentralization.service.TodoService;

import java.util.List;


@RestController
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping("/todos/{id}")
    public ResponseEntity<Todo> getTodoById(@PathVariable Long id) {
        Todo todoData = this.todoService.getTodoById(id);
        return ResponseEntity.ok().body(todoData);
    }
    @GetMapping("/todos")
    public ResponseEntity<List<Todo>> getTodos() {
        List<Todo> listTodo = this.todoService.handleGetTodo();
        return ResponseEntity.ok().body(listTodo);
    }
    @PostMapping("/todos")
    public ResponseEntity<Todo> createTodo(@RequestBody Todo input) {
        Todo newTodo = this.todoService.handleCreateTodo(input);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTodo);
    }
    @PutMapping("/todos/{id}")
    public ResponseEntity<String> updateTodo(@PathVariable Long id, @RequestBody Todo input) {
        this.todoService.handleUpdateTodo(id, input);
        return ResponseEntity.ok().body("sucess");
    }
    @DeleteMapping("/todos/{id}")
    public ResponseEntity<String> deleteTodo(@PathVariable Long id) {
        this.todoService.handleDeleteTodo(id);
        return ResponseEntity.ok().body("success");
    }
}