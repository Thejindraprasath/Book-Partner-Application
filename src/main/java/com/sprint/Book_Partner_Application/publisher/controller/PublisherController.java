package com.sprint.Book_Partner_Application.publisher.controller;

import com.sprint.Book_Partner_Application.publisher.entity.Publisher;
import com.sprint.Book_Partner_Application.publisher.repository.PublisherRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/publisher")
public class PublisherController {

    private final PublisherRepository repo;

    public PublisherController(PublisherRepository repo) {
        this.repo = repo;
    }

    @PostMapping("/add")
    public Publisher addPublisher(@RequestBody Publisher publisher) {
        return repo.save(publisher);
    }

    @GetMapping("/get")
    public List<Publisher> getAll() {
        return repo.findAll();
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable String id) {
        repo.deleteById(id);
        return "Publisher Deleted Successfully";
    }
}