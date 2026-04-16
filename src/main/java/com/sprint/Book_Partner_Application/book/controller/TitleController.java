package com.sprint.Book_Partner_Application.book.controller;

import com.sprint.Book_Partner_Application.book.entity.Title;
import com.sprint.Book_Partner_Application.book.repository.TitleRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book")
public class TitleController {

        private final TitleRepository repo;

        public TitleController(TitleRepository repo) {
            this.repo = repo;
        }

        @PostMapping("/add")
        public Title addBook(@RequestBody Title book) {
            return repo.save(book);
        }

        @GetMapping("/get")
        public List<Title> getAll() {
            return repo.findAll();
        }

        @DeleteMapping("/delete/{id}")
        public String delete(@PathVariable String id) {
            repo.deleteById(id);
            return "Book Deleted Successfully";
        }
}
