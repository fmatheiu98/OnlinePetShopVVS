package com.example.vvs.webapp.repository;

import com.example.vvs.webapp.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepo extends JpaRepository<Product,Integer> {
    Product getByName(String name);
}
