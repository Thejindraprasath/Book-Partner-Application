package com.sprint.Book_Partner_Application.author.controller;

import com.sprint.Book_Partner_Application.author.entity.Author;
import com.sprint.Book_Partner_Application.author.repository.AuthorRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping ("/author")
public class AuthorController {

    private final AuthorRepository repo;

    public AuthorController(AuthorRepository repo) {
        this.repo = repo;
    }

    @PostMapping ("/add")
    public Author addAuthor(@RequestBody Author author) {
        return repo.save(author);
    }

    @GetMapping("/get")
    public List<Author> getAll() {
        return repo.findAll();
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable  String id) {
        repo.deleteById(id);
        return "Author Deleted Successfully";
    }
}
