package com.sprint.Book_Partner_Application.store.controller;

import com.sprint.Book_Partner_Application.store.entity.Store;
import com.sprint.Book_Partner_Application.store.repository.StoreRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/store")
public class StoreController {

    private final StoreRepository repo;

    public StoreController(StoreRepository repo) {
        this.repo = repo;
    }

    @PostMapping("/add")
    public Store addStore(@RequestBody Store store) {
        return repo.save(store);
    }

    @GetMapping("/get")
    public List<Store> getAll() {
        return repo.findAll();
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable String id) {
        repo.deleteById(id);
        return "Store Deleted Successfully";
    }
}