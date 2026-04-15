package com.sprint.Book_Partner_Application.sales.controller;

import com.sprint.Book_Partner_Application.sales.entity.Sale;
import com.sprint.Book_Partner_Application.sales.repository.SaleRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sales")
public class SalesController {

    private final SaleRepository repo;

    public SalesController(SaleRepository repo) {
        this.repo = repo;
    }

    @PostMapping("/add")
    public Sale addSale(@RequestBody Sale sale) {
        return repo.save(sale);
    }

    @GetMapping("/get")
    public List<Sale> getAll() {
        return repo.findAll();
    }

//    @DeleteMapping("/delete/{id}")
//    public String delete(@PathVariable String id) {
//        repo.deleteById(id);
//        return "Sale Deleted Successfully";
//    }
}
