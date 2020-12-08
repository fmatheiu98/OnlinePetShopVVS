package com.example.vvs.webapp.controller;

import com.example.vvs.webapp.model.Product;
import com.example.vvs.webapp.repository.ProductRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.StringContains.containsString;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.sharedHttpSession;

@TestPropertySource(locations="classpath:application-test.properties")
@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepo productRepo;

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
        p1.setId(1);
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
    }

    @Test
    void test_viewProductWithInvalidID () throws Exception
    {
        mockMvc.perform(MockMvcRequestBuilders.get("/product/10")).andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));
    }

    @Test
    void test_addToCartWorkingFine() throws Exception
    {
        Product p1 = new Product("testP11","testD11",100,"testP11");
        Product p2 = new Product("testP22","testD22",101,"testP22");
        p1.setId(1);
        p2.setId(2);
        //adaug 2 produse in repo
        productRepo.save(p1);
        productRepo.save(p2);

        //creez un cart initial, cu testkey, pret 4 si 4 bucati
        Map<String,Map<Integer,Integer>> cart = new HashMap<>();
        Map<Integer,Integer> val = new HashMap<>();
        val.put(4,4);
        cart.put("testkey",val);

        //mockmvc, cu pastrarea sesiunii intre apeluri
        MockMvc mockMvc2 = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(sharedHttpSession())
                .build();

        mockMvc2.perform(MockMvcRequestBuilders.get("/addToCart/1").sessionAttr("prodsession",cart)).andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));

        //creez cart-ul care ar trebui sa fie dupa add to cart
        Map<String,Map<Integer,Integer>> cart2 = new HashMap<>();
        Map<Integer,Integer> val2 = new HashMap<>();
        val2.put(4,4);
        cart2.put("testkey",val2);
        Map<Integer,Integer> val3 = new HashMap<>();
        val3.put(100,1);
        cart2.put("testP11",val3);

        //request pt. verificare pagina principala
        mockMvc2.perform(MockMvcRequestBuilders.get("/")).andDo(print())
                .andExpect(model().attribute("cart",cart2))
                .andExpect(model().attribute("sum",116))
                .andExpect(model().attribute("noItems",5))
                .andExpect(model().attribute("listProducts", hasItem(
                        allOf(
                                hasProperty("name", is("testP11")),
                                hasProperty("description", is("testD11")),
                                hasProperty("price", is(100))
                        )
                )))
                .andExpect(model().attribute("listProducts", hasItem(
                        allOf(
                                hasProperty("name", is("testP22")),
                                hasProperty("description", is("testD22")),
                                hasProperty("price", is(101))
                        )
                )))
                .andExpect(request().sessionAttribute("prodsession",cart2));

        //request pt. verificare pagina de cart
        mockMvc2.perform(MockMvcRequestBuilders.get("/cart")).andDo(print())
                .andExpect(model().attribute("cart",cart2))
                .andExpect(model().attribute("sum",116))
                .andExpect(model().attribute("noItems",5))
                .andExpect(request().sessionAttribute("prodsession",cart2));


        //alt test pentru addtocart
        MockMvc mockMvc3 = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(sharedHttpSession())
                .build();

        //adaug 2 produse la un cart gol
        mockMvc3.perform(MockMvcRequestBuilders.get("/addToCart/2")).andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));
        mockMvc3.perform(MockMvcRequestBuilders.get("/addToCart/2")).andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));

        Map<String,Map<Integer,Integer>> cart_f = new HashMap<>();
        Map<Integer,Integer> val_f = new HashMap<>();
        val_f.put(101,2);
        cart_f.put("testP22",val_f);

        //test complet
        mockMvc3.perform(MockMvcRequestBuilders.get("/")).andDo(print())
                .andExpect(model().attribute("cart",cart_f))
                .andExpect(model().attribute("sum",202))
                .andExpect(model().attribute("noItems",2))
                .andExpect(model().attribute("listProducts", hasItem(
                        allOf(
                                hasProperty("name", is("testP22")),
                                hasProperty("description", is("testD22")),
                                hasProperty("price", is(101))
                        )
                )))
                .andExpect(request().sessionAttribute("prodsession",cart_f));
    }

    @Test
    void test_addToCartWithInexistentProductID() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/addToCart/100")).andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));
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

    @Test
    void test_cartControllerWhenSessionAttrCartIsNULL() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/cart")).andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));
    }

    @Test
    void test_deleteFromCartGoodCase() throws Exception {

        Map<String,Map<Integer,Integer>> cart = new HashMap<>();
        Map<Integer,Integer> val = new HashMap<>();
        val.put(30,2);
        cart.put("cartProd1",val);
        val = new HashMap<>();
        val.put(31,3);
        cart.put("cartProd2",val);

        //mockmvc, cu pastrarea sesiunii intre apeluri
        MockMvc mockMvc2 = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(sharedHttpSession())
                .build();

        mockMvc2.perform(MockMvcRequestBuilders.get("/delete?key=cartProd2").sessionAttr("prodsession",cart)).andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/cart"));

        //cart-ul dupa stergere
        Map<String,Map<Integer,Integer>> cart_f = new HashMap<>();
        Map<Integer,Integer> val_f = new HashMap<>();
        val_f.put(30,2);
        cart_f.put("cartProd1",val_f);

        mockMvc2.perform(MockMvcRequestBuilders.get("/cart")).andDo(print())
                .andExpect(model().attribute("cart",cart_f))
                .andExpect(model().attribute("sum",60))
                .andExpect(model().attribute("noItems",2))
                .andExpect(request().sessionAttribute("prodsession",cart));
    }

    @Test
    void test_deleteFromCartBAD_Case() throws Exception {

        //cand nu exista atributul de sesiune cart
        mockMvc.perform(MockMvcRequestBuilders.get("/delete?key=Colivie")).andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/cart"));


        //cart-ul initial
        Map<String,Map<Integer,Integer>> cart_f = new HashMap<>();
        Map<Integer,Integer> val_f = new HashMap<>();
        val_f.put(30,2);
        cart_f.put("cartProd1",val_f);

        //cand nu este produsul dat in cos
        mockMvc.perform(MockMvcRequestBuilders.get("/delete?key=inexistent").sessionAttr("prodsession",cart_f)).andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/cart"));


    }

    @Test
    void test_addToCartOne_GoodCase() throws Exception {
        Product p1 = (new Product("testP1","testD1",45,"testP1"));
        Product p2 = (new Product("testP2","testD2",46,"testP2"));
        p1.setId(1);
        p1.setId(2);
        productRepo.save(p1);
        productRepo.save(p2);

        MockMvc mockMvc2 = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(sharedHttpSession())
                .build();

        //cart-ul initial
        Map<String,Map<Integer,Integer>> cart_f = new HashMap<>();
        Map<Integer,Integer> val_f = new HashMap<>();
        val_f.put(45,2);
        cart_f.put("testP1",val_f);
        val_f = new HashMap<>();
        val_f.put(100,4);
        cart_f.put("inCos",val_f);

        //request pe addToCartOne
        mockMvc2.perform(MockMvcRequestBuilders.get("/addToCartOne/1").sessionAttr("prodsession",cart_f)).andDo(print())
                .andExpect(redirectedUrl("/cart"));

        //cart-ul dupa adaugare
        Map<Integer,Integer> map = new HashMap<>();
        Map<Integer,Integer> hashMap = cart_f.get("testP1");
        Map.Entry<Integer,Integer> entry = hashMap.entrySet().iterator().next();
        map.put(45,entry.getValue() + 1);
        cart_f.put("testP1",map);

        //test pentru adaugarea +1 in cart
        mockMvc2.perform(MockMvcRequestBuilders.get("/cart"))
                .andExpect(request().sessionAttribute("prodsession",cart_f))
                .andExpect(model().attribute("cart",cart_f))
                .andExpect(model().attribute("sum",535))
                .andExpect(model().attribute("noItems",7));
    }

    @Test
    void test_addToCartOne_BadCases() throws Exception {
        MockMvc mockMvc2 = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(sharedHttpSession())
                .build();

        Map<String,Map<Integer,Integer>> cart_f2 = new HashMap<>();

        //test cand cart-ul este gol
        mockMvc2.perform(MockMvcRequestBuilders.get("/addToCartOne/1").sessionAttr("prodsession",cart_f2)).andDo(print())
                .andExpect(redirectedUrl("/cart"))
                .andExpect(request().sessionAttribute("prodsession",cart_f2));

        //cart-ul initial, la testul 2
        Map<String,Map<Integer,Integer>> cart_f = new HashMap<>();
        Map<Integer,Integer> val_f = new HashMap<>();
        val_f.put(45,5);
        cart_f.put("testP1",val_f);

        MockMvc mockMvc3 = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(sharedHttpSession())
                .build();

        //produsul cu id 10 nu se gaseste in cart
        mockMvc3.perform(MockMvcRequestBuilders.get("/addToCartOne/10").sessionAttr("prodsession",cart_f)).andDo(print())
                .andExpect(redirectedUrl("/cart"))
                .andExpect(request().sessionAttribute("prodsession",cart_f));
    }

    @Test
    void test_deleteOne_GoodCase() throws Exception {

        Product p1 = (new Product("testP1","testD1",45,"testP1"));
        Product p2 = (new Product("testP2","testD2",100,"testP2"));
        p1.setId(1);
        p1.setId(2);
        productRepo.save(p1);
        productRepo.save(p2);

        MockMvc mockMvc2 = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(sharedHttpSession())
                .build();

        //cart-ul initial
        Map<String,Map<Integer,Integer>> cart_f = new HashMap<>();

        Map<Integer,Integer> val_f = new HashMap<>();
        val_f.put(45,4);
        cart_f.put("testP1",val_f);

        val_f = new HashMap<>();
        val_f.put(200,5);
        cart_f.put("testP2",val_f);


        mockMvc2.perform(MockMvcRequestBuilders.get("/deleteOne/1").sessionAttr("prodsession",cart_f)).andDo(print())
                .andExpect(redirectedUrl("/cart"));

        //cart-ul dupa stergere
        Map<Integer,Integer> map = new HashMap<>();
        Map<Integer,Integer> hashMap = cart_f.get("testP1");
        Map.Entry<Integer,Integer> entry = hashMap.entrySet().iterator().next();
        System.out.println(entry.getValue());
        map.put(45,entry.getValue());
        cart_f.put("testP1",map);

        mockMvc2.perform(MockMvcRequestBuilders.get("/cart")).andDo(print())
                .andExpect(request().sessionAttribute("prodsession",cart_f))
                .andExpect(model().attribute("cart",cart_f))
                .andExpect(model().attribute("sum",1135))
                .andExpect(model().attribute("noItems",8));
    }

    @Test
    void test_deleteOne_BadCases() throws Exception {
        MockMvc mockMvc2 = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(sharedHttpSession())
                .build();

        Map<String,Map<Integer,Integer>> cart_f2 = new HashMap<>();

        //test cand cart-ul este gol
        mockMvc2.perform(MockMvcRequestBuilders.get("/deleteOne/1").sessionAttr("prodsession",cart_f2)).andDo(print())
                .andExpect(redirectedUrl("/cart"))
                .andExpect(request().sessionAttribute("prodsession",cart_f2));

        //cart-ul initial, la testul 2
        Map<String,Map<Integer,Integer>> cart_f = new HashMap<>();
        Map<Integer,Integer> val_f = new HashMap<>();
        val_f.put(45,5);
        cart_f.put("testP1",val_f);

        MockMvc mockMvc3 = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(sharedHttpSession())
                .build();

        //produsul cu id 10 nu se gaseste in cart
        mockMvc3.perform(MockMvcRequestBuilders.get("/deleteOne/10").sessionAttr("prodsession",cart_f)).andDo(print())
                .andExpect(redirectedUrl("/cart"));

        //cart-ul a ramas la fel
        mockMvc3.perform(MockMvcRequestBuilders.get("/cart")).andDo(print())
                .andExpect(request().sessionAttribute("prodsession",cart_f))
                .andExpect(model().attribute("cart",cart_f))
                .andExpect(model().attribute("sum",225))
                .andExpect(model().attribute("noItems",5));
    }
}