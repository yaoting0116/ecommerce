package com.esun.ecommerce.service;

import com.esun.ecommerce.entity.Product;
import com.esun.ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repo;

    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    public List<Product> getAvailableProducts() {
        return repo.findAll();
    }

    public Product save(Product p) {
        return repo.save(p);
    }
}
