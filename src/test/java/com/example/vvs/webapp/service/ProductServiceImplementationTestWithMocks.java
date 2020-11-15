package com.example.vvs.webapp.service;

import com.example.vvs.webapp.model.Product;
import com.example.vvs.webapp.repository.ProductRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplementationTestWithMocks {

    @Mock
    private ProductRepo productRepository;

    @InjectMocks
    private ProductServiceImplementation productService;

    @BeforeEach
    private void beforeEach()
    {
        productRepository.deleteAll();
    }

    @AfterEach
    private void afterAll()
    {
        productRepository.deleteAll();
    }

    @Test
    @DisplayName("ProductServiceImplementation Test")
    void getAllTheProducts() {
        Product p = new Product();
        p.setPrice(100);
        List<Product> productList = new ArrayList<>(1);
        productList.add(p);

        when(productRepository.findAll()).thenReturn(productList);

        List<Product> foundproductList = productService.getAllTheProducts();

        assertThat(foundproductList).hasSize(1);
        assertThat(foundproductList.get(0)).isEqualTo(p);
    }

    @Test
    void findByNameWithMockRepo() {
        Product p = new Product();
        p.setPrice(101);
        when(productRepository.getByName(any(String.class))).thenReturn(p);

        Product foundProduct = productService.findByName("test");

        verify(productRepository).getByName(any(String.class));
        assertThat(foundProduct).isNotNull();
        assertThat(foundProduct.getPrice()).isEqualTo(p.getPrice());
    }

    @Test
    void findByNameWithMockRepo_withNullArgument() {

        assertThrows(IllegalArgumentException.class,()-> productService.findByName(""));
        assertThrows(IllegalArgumentException.class,()-> productService.findByName(null));
    }

    @Test
    void findByIdWithMockRepo() {
        Product p = new Product();
        p.setDescription("myTestDescription");

        when(productRepository.findById(any(Integer.class))).thenReturn(java.util.Optional.of(p));

        Product foundProduct = productService.findById(123);

        verify(productRepository).findById(any(Integer.class));
        assertThat(foundProduct).isNotNull();
        assertThat(foundProduct.getDescription()).isEqualTo("myTestDescription");
    }
}