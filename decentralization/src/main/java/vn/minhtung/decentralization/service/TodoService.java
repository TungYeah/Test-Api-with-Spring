package vn.minhtung.decentralization.service;

import org.springframework.stereotype.Service;
import vn.minhtung.decentralization.entity.Todo;
import vn.minhtung.decentralization.repository.TodoRepository;

import java.util.List;
import java.util.Optional;
@Service
public class TodoService {
    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public Todo getTodoById(Long id) {
        Optional<Todo> todoOptional = this.todoRepository.findById(id);
        return todoOptional.isPresent() ? todoOptional.get() : null;
    }

    public Todo handleCreateTodo(Todo todo) {
        Todo createdTodo = this.todoRepository.save(todo);
        return createdTodo;
    }

    public List<Todo> handleGetTodo() {
        return this.todoRepository.findAll();
    }

    public void handleUpdateTodo(long id, Todo inputTodo) {

        Optional<Todo> todoOptional = this.todoRepository.findById(id);
        if (todoOptional.isPresent()) {
            Todo currentTodo = todoOptional.get();

            currentTodo.setCompleted(inputTodo.isCompleted());
            currentTodo.setUsername(inputTodo.getUsername());

            this.todoRepository.save(currentTodo);
        }
    }

    public void handleDeleteTodo(Long id) {
        this.todoRepository.deleteById(id);
    }
}
