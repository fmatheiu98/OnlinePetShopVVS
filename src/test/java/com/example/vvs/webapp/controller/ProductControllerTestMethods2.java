package com.example.vvs.webapp.controller;

import com.example.vvs.webapp.model.Product;
import com.example.vvs.webapp.repository.ProductRepo;
import com.example.vvs.webapp.service.ProductServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ProductControllerTestMethods2 {

    @Mock
    private Model model;

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private ProductRepo productRepo;

    private ProductServiceImplementation productServiceImplementation;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void after()
    {
        httpSession.removeAttribute("prodsession");
    }

    @Test
    void testDeleteFromCartOnly1ProductWhereThereIsMoreThan1Product()
    {
        productServiceImplementation  = new ProductServiceImplementation(productRepo);
        productController = new ProductController(productServiceImplementation);
        Product p1 = new Product("testP1","testD1",45,"testP1");

        productRepo.save(p1);
        Map<String, Map<Integer,Integer>> cartInitial = new HashMap<>();
        Map<Integer,Integer> val = new HashMap<>();
        val.put(45,4);
        cartInitial.put("testP1",val);

        httpSession.setAttribute("prodsession",cartInitial);

        String res = productController.deleteOne(1,model,httpSession);
        Map<String, Map<Integer,Integer>> cart = (Map<String, Map<Integer,Integer>>)httpSession.getAttribute("prodsession");

        Map.Entry<String,Map<Integer,Integer>> entry = cart.entrySet().iterator().next();
        Map<Integer,Integer> intrare1 = entry.getValue();

        //numarul de produse de tipul testP1 este cel corect dupa stergere
        assertEquals(3,intrare1.get(45));

        Map<String, Map<Integer,Integer>> cart1 = new HashMap<>();
        Map<Integer,Integer> val1 = new HashMap<>();
        val1.put(45,3);
        cart1.put("testP1",val1);

        verify(model, times(1)).addAttribute("cart",cart1);
        verify(model, times(1)).addAttribute("sum",135);
        verify(model, times(1)).addAttribute("noItems",3);
        assertEquals("redirect:/cart",res);
    }

    @Test
    void testDeleteFromCartOnly1ProductWhereThereOnly1Product()
    {
        productServiceImplementation  = new ProductServiceImplementation(productRepo);
        productController = new ProductController(productServiceImplementation);
        Product p1 = new Product("testP1","testD1",45,"testP1");

        productRepo.save(p1);
        Map<String, Map<Integer,Integer>> cartInitial = new HashMap<>();
        Map<Integer,Integer> val = new HashMap<>();
        val.put(45,1);
        cartInitial.put("testP1",val);

        httpSession.setAttribute("prodsession",cartInitial);

        String res = productController.deleteOne(1,model,httpSession);
        Map<String, Map<Integer,Integer>> cart = (Map<String, Map<Integer,Integer>>)httpSession.getAttribute("prodsession");

        assertEquals(0,cart.size());
        assertEquals("redirect:/cart",res);
    }
}
