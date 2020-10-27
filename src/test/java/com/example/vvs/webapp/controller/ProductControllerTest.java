package com.example.vvs.webapp.controller;

import com.example.vvs.webapp.model.Product;
import com.example.vvs.webapp.repository.ProductRepo;
import com.example.vvs.webapp.service.ProductService;
import com.example.vvs.webapp.service.ProductServiceImplementation;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.ui.Model;
import org.springframework.web.util.NestedServletException;

import javax.servlet.http.HttpSession;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductServiceImplementation productServiceImplementation;

    @Mock
    private Model model;

    @Mock
    private MockHttpSession httpSession;

    @Autowired
    private ProductRepo productRepo;

    @InjectMocks
    private ProductController productController;

    @Test
    void testViewProductsWorkingFine() throws Exception{
        Product p1 = (new Product("testP1","testD1",45,"testP1"));
        Product p2 = (new Product("testP2","testD2",46,"testP2"));

        productRepo.save(p1);
        productRepo.save(p2);

        Map<String,Map<Integer,Integer>> cart = new HashMap<>();
        Map<Integer,Integer> val = new HashMap<>();
        val.put(4,4);
        cart.put("testkey",val);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/")
                .sessionAttr("prodsession",cart))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("listProducts"))
                .andExpect(content().string(containsString("Home Page")))
                .andExpect(model().attribute("listProducts", hasItem(
                        allOf(
                                hasProperty("name", is("testP1")),
                                hasProperty("description", is("testD1")),
                                hasProperty("price", is(45))
                        )
                )))
                .andExpect(model().attribute("listProducts", hasItem(
                        allOf(
                                hasProperty("name", is("testP2")),
                                hasProperty("description", is("testD2")),
                                hasProperty("price", is(46))
                        )
                )))
                .andExpect(model().attributeExists("cart"))
                .andExpect(model().attribute("cart",cart))
                .andExpect(model().attribute("sum",is(16)))
                .andExpect(model().attribute("noItems",is(4)));
    }

    @Test
    public void whenGetHomePage_then200WithContent() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("listProducts"))
                .andExpect(content().string(containsString("Home Page")));
    }

    @Test
    void test_viewProductWorkingFine () throws Exception
    {
        Product p1 = (new Product("testP1","testD1",45,"testP1"));
        productRepo.save(p1);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/product/1")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("product"))
                .andExpect(content().string(containsString("Product Details")))
                .andExpect(model().attributeExists("listProducts","myProduct"))
                .andExpect(model().attribute("listProducts", hasItem(
                        allOf(
                                hasProperty("name", is("testP1")),
                                hasProperty("description", is("testD1")),
                                hasProperty("price", is(45))
                        )
                )))
                .andExpect(model().attribute("myProduct",allOf(
                        hasProperty("name",is("testP1")),
                        hasProperty("description",is("testD1")),
                        hasProperty("price",is(45)))));

        //get request pe un url inexistent, deoarece nu am introdus un al doilea produs in repository
        //assertThrows(NestedServletException.class,()-> this.mockMvc.perform(MockMvcRequestBuilders.get("/product/2")));
    }

    @Test
    void test_addToCartWorkingFine() throws Exception
    {
        Product p1 = (new Product("testP1","testD1",45,"testP1"));
        Product p2 = (new Product("testP2","testD2",46,"testP2"));

        productRepo.save(p1);
        productRepo.save(p2);

        Map<String,Map<Integer,Integer>> cart = new HashMap<>();
        Map<Integer,Integer> val = new HashMap<>();
        val.put(4,4);
        cart.put("testkey",val);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/addToCart/1").sessionAttr("prodsession",cart)).andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));

        //with no product added to cart before, so the prodsession(cart) is null
        this.mockMvc.perform(MockMvcRequestBuilders.post("/addToCart/1"))
                .andDo(print())
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void test_cartControllerWorkingFine() throws Exception
    {
        Product p1 = (new Product("testP1","testD1",45,"testP1"));
        Product p2 = (new Product("testP2","testD2",46,"testP2"));

        productRepo.save(p1);
        productRepo.save(p2);

        Map<String,Map<Integer,Integer>> cart = new HashMap<>();
        Map<Integer,Integer> val = new HashMap<>();
        val.put(31,5);
        cart.put("cartProd1",val);
        val = new HashMap<>();
        val.put(32,6);
        cart.put("cartProd2",val);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/cart")
                .sessionAttr("prodsession",cart))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("cart"))
                .andExpect(model().attributeExists("listProducts","sum","noItems","cart"))
                .andExpect(content().string(containsString("Your Cart")))
                .andExpect(model().attribute("listProducts", hasItem(
                        allOf(
                                hasProperty("name", is("testP1")),
                                hasProperty("description", is("testD1")),
                                hasProperty("price", is(45))
                        )
                )))
                .andExpect(model().attribute("listProducts", hasItem(
                        allOf(
                                hasProperty("name", is("testP2")),
                                hasProperty("description", is("testD2")),
                                hasProperty("price", is(46))
                        )
                )))
                .andExpect(model().attribute("cart",cart))
                .andExpect(model().attribute("sum",is(347)))
                .andExpect(model().attribute("noItems",is(11)));
    }

}