package com.in28minutes.springboot.myfirstwebapp.todo;

import jakarta.validation.Valid;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@SessionAttributes("name")
public class TodoControllerJPA {

    private TodoService todoService;

    private TodoRepository todoRepository;

    public TodoControllerJPA(TodoService todoService, TodoRepository todoRepository) {
        this.todoService = todoService;
        this.todoRepository = todoRepository;
    }

    @RequestMapping("list-todos")
    public String listAllTodos(ModelMap model) {
        //List<Todo> todos= todoService.findByUsername(getLoggedInUsername(model));
        List<Todo> todos= todoRepository.findByUsername(getLoggedInUsername(model));
        model.addAttribute("todos",todos);
        return "listTodos";
    }

    private static String getLoggedInUsername(ModelMap model) {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @RequestMapping(value = "add-todo",method= RequestMethod.GET)
    public String showNewTodoPage(ModelMap model) {
        String username= getLoggedInUsername(model);
        Todo todo= new Todo(0,username,"",LocalDate.now().plusYears(1),false);
        model.put("todo",todo);
        return "todo";
    }

    @RequestMapping(value = "add-todo",method= RequestMethod.POST)
    public String addNewTodoPage(ModelMap model, @Valid Todo todo, BindingResult result) {
        if (result.hasErrors()) {
            return "todo";
        }

        todoService.addToDo(getLoggedInUsername(model),todo.getDescription(), todo.getTargetDate(),false);
        return "redirect:list-todos";
    }


    @RequestMapping(value = "delete-todo")
    public String deleteTodo(@RequestParam int id) {
        todoService.deleteById(id);
        return "redirect:list-todos";
    }

    @RequestMapping(value = "update-todo",method= RequestMethod.GET)
    public String showUpdateTodo(@RequestParam int id,ModelMap model) {
        Todo todo=todoService.findById(id);
        model.put("todo",todo);
        return "todo";
    }

    @RequestMapping(value = "update-todo",method= RequestMethod.POST)
    public String updateTodoPage(ModelMap model, @Valid Todo todo, BindingResult result) {
        if (result.hasErrors()) {
            return "todo";
        }

        String username = getLoggedInUsername(model);
        todo.setUsername(username);
        todoService.updateToDo(todo);
        return "redirect:list-todos";
    }


}
