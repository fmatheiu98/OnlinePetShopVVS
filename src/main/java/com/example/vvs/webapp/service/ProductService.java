package com.example.vvs.webapp.service;

import com.example.vvs.webapp.model.Product;

import java.util.List;

public interface ProductService {
    List<Product> getAllTheProducts();
    Product findByName(String name);
    Product findById(int id);
}
