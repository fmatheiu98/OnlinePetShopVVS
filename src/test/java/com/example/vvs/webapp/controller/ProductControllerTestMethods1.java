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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ProductControllerTestMethods1 {

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
    void testAddToCartWhenProdSessionIsNULL()
    {
        productServiceImplementation  = new ProductServiceImplementation(productRepo);
        productController = new ProductController(productServiceImplementation);
        Product p1 = new Product("testP1","testD1",45,"testP1");

        productRepo.save(p1);

        Map<String, Map<Integer,Integer>> cart = new HashMap<>();
        Map<Integer,Integer> val = new HashMap<>();
        val.put(45,1);
        cart.put(p1.getName(),val);

        String res = productController.addToCart(1,model,httpSession);

        verify(model, times(1)).addAttribute("cart", cart);
        verify(model, times(1)).addAttribute("sum", 45);
        verify(model, times(1)).addAttribute("noItems", 1);

        assertThat(httpSession.getAttribute("prodsession")).isEqualTo(cart);
        assertEquals("redirect:/",res);
    }

    @Test
    void testAddToCartWhenProdSessionHasAddedCartItems()
    {
        productServiceImplementation  = new ProductServiceImplementation(productRepo);
        productController = new ProductController(productServiceImplementation);
        Product p1 = new Product("testP1","testD1",45,"testP1");
        Product p2 = new Product("testP2","testD2",50,"testP2");
        Product pInit = new Product("testPinit","testDinit",100,"testPinit");

        productRepo.save(pInit);
        productRepo.save(p1);
        productRepo.save(p2);

        //ceea ce se afla in sesiune inainte de apelul addToCart
        Map<String, Map<Integer,Integer>> cartInitial = new HashMap<>();
        Map<Integer,Integer> val = new HashMap<>();
        val.put(100,3);
        cartInitial.put("testPinit",val);
        httpSession.setAttribute("prodsession",cartInitial);

        //ceea ce vrem sa fie in cart dupa addToCart(adaugat in plus fata de cel initial)

        productController.addToCart(2,model,httpSession);

        Map<String, Map<Integer,Integer>> cart = new HashMap<>();
        val = new HashMap<>();
        val.put(100,3);
        cart.put("testPinit",val);

        val = new HashMap<>();
        val.put(45,1);
        cart.put(p1.getName(),val);

        verify(model, times(1)).addAttribute("cart",cart);
        verify(model, times(1)).addAttribute("sum", 345);
        verify(model, times(1)).addAttribute("noItems", 4);

        productController.addToCart(2,model,httpSession);
        val = new HashMap<>();
        val.put(45,2);
        cart.put(p1.getName(),val);

        verify(model, times(2)).addAttribute("cart",cart);
        verify(model, times(1)).addAttribute("sum", 390);
        verify(model, times(1)).addAttribute("noItems", 5);

        String res = productController.addToCart(3,model,httpSession);
        val = new HashMap<>();
        val.put(50,1);
        cart.put(p2.getName(),val);

        verify(model, times(3)).addAttribute("cart",cart);
        verify(model, times(1)).addAttribute("sum", 440);
        verify(model, times(1)).addAttribute("noItems", 6);

        assertThat(httpSession.getAttribute("prodsession")).isEqualTo(cart);
        assertEquals("redirect:/",res);
    }

    @Test
    void testDeleteFromCart()
    {
        productServiceImplementation  = new ProductServiceImplementation(productRepo);
        productController = new ProductController(productServiceImplementation);
        Product p1 = new Product("testP1","testD1",45,"testP1");
        Product p2 = new Product("testP2","testD2",50,"testP2");
        Product p3 = new Product("testP3","testD3",60,"testP3");

        productRepo.save(p1);
        productRepo.save(p2);
        productRepo.save(p3);

        //ceea ce se afla in sesiune inainte de apelul deleteFromCart
        Map<String, Map<Integer,Integer>> cartInitial = new HashMap<>();
        Map<Integer,Integer> val = new HashMap<>();
        val.put(45,1);
        cartInitial.put("testP1",val);

        val = new HashMap<>();
        val.put(50,1);
        cartInitial.put("testP2",val);

        val = new HashMap<>();
        val.put(60,1);
        cartInitial.put("testP3",val);
        httpSession.setAttribute("prodsession",cartInitial);

        String res = productController.deleteFromCart("testP2",httpSession,model);

        Map<String, Map<Integer,Integer>> cart = (Map<String, Map<Integer,Integer>>)httpSession.getAttribute("prodsession");

        assertThat(cart.keySet().containsAll(Arrays.asList("testP1","testP3"))).isTrue();

        verify(model, times(1)).addAttribute("cart",cartInitial);

        assertEquals("redirect:/cart",res);
    }

    @Test
    void test_addToCartOnly1MoreProduct()
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

        String res = productController.addToCartOne(1,model,httpSession);
        Map<String, Map<Integer,Integer>> cart = (Map<String, Map<Integer,Integer>>)httpSession.getAttribute("prodsession");

        Map.Entry<String,Map<Integer,Integer>> entry = cart.entrySet().iterator().next();
        Map<Integer,Integer> intrare1 = entry.getValue();

        //numarul de produse de tipul testP1 este cel corect dupa adaugare
        assertEquals(2,intrare1.get(45));

        Map<String, Map<Integer,Integer>> cart1 = new HashMap<>();
        Map<Integer,Integer> val1 = new HashMap<>();
        val1.put(45,2);
        cart1.put("testP1",val1);

        verify(model, times(1)).addAttribute("cart",cart1);
        verify(model, times(1)).addAttribute("sum",90);
        verify(model, times(1)).addAttribute("noItems",2);
        assertEquals("redirect:/cart",res);
    }


}
