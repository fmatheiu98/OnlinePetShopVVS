package com.example.vvs.webapp.service;

import com.example.vvs.webapp.model.Product;
import com.example.vvs.webapp.repository.ProductRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImplementation implements ProductService{

    private ProductRepo productRepository;

    public ProductServiceImplementation(ProductRepo productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getAllTheProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product findByName(String name) {
        if(name==null || name.equals(""))
        {
            throw new IllegalArgumentException();
        }
        return productRepository.getByName(name);
    }

    @Override
    public Product findById(int id) {
        return productRepository.findById(id).orElse(null);
    }

}
