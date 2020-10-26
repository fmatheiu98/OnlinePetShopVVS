package com.example.vvs.webapp.controller;

import com.example.vvs.webapp.model.Product;
import com.example.vvs.webapp.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    public String viewProducts(Model model, HttpSession session)
    {
        if(model==null)
        {
            throw new IllegalArgumentException();
        }

        Map<String, Map<Integer,Integer>> cart = (Map<String, Map<Integer,Integer>>) session.getAttribute("prodsession");
        if(cart!=null) {
            model.addAttribute("cart", cart);
            int sum = 0;
            int no_items = 0;
            for (Map<Integer, Integer> val : cart.values()) {
                Map.Entry<Integer,Integer> entry = val.entrySet().iterator().next();
                sum += entry.getValue() * entry.getKey();
                no_items+= entry.getValue();
            }
            model.addAttribute("sum", sum);
            model.addAttribute("noItems", no_items);
        }

        model.addAttribute("listProducts",productService.getAllTheProducts());
        return "index";
    }

    @RequestMapping("/product/{id}")
    public String viewProduct(@PathVariable int id,Model model, HttpSession session)
    {
        Product p = productService.findById(id);
        model.addAttribute("myProduct",p);
        model.addAttribute("listProducts",productService.getAllTheProducts());
        return "product";
    }

    @RequestMapping("/addToCart/{id}")
    public String addToCart(@PathVariable int id, Model model, HttpSession session) {

        Product p = productService.findById(id);

        if (session.getAttribute("prodsession") == null) {

            Map<String, Map<Integer,Integer>> cart = new HashMap<>();
            Map<Integer,Integer> map = new HashMap<>();
            map.put(p.getPrice(),1);
            cart.put(p.getName(),map);

            session.setAttribute("prodsession", cart);
            model.addAttribute("cart", cart);
            int sum = 0;
            int no_items = 0;
            for (Map<Integer, Integer> val : cart.values()) {
                Map.Entry<Integer,Integer> entry = val.entrySet().iterator().next();
                sum += entry.getValue() * entry.getKey();
                no_items+= entry.getValue();
            }
            model.addAttribute("sum", sum);
            model.addAttribute("noItems", no_items);
        } else {

            Map<String, Map<Integer,Integer>> cart = (Map<String, Map<Integer,Integer>>) session.getAttribute("prodsession");
            Map<Integer,Integer> map = new HashMap<>();

            if(cart.containsKey(p.getName()))
            {
                Map<Integer,Integer> hashMap = cart.get(p.getName());
                Map.Entry<Integer,Integer> entry = hashMap.entrySet().iterator().next();
                map.put(p.getPrice(),entry.getValue() + 1);
            }
            else{
                map.put(p.getPrice(),1);
            }

            cart.put(p.getName(),map);
            session.setAttribute("prodsession", cart);
            model.addAttribute("cart", cart);
            int sum = 0;
            int no_items = 0;
            for (Map<Integer, Integer> val : cart.values()) {
                Map.Entry<Integer,Integer> entry = val.entrySet().iterator().next();
                sum += entry.getValue() * entry.getKey();
                no_items+= entry.getValue();
            }
            model.addAttribute("sum", sum);
            model.addAttribute("noItems", no_items);
        }
        return "redirect:/";
    }

    @RequestMapping("/cart")
    public String cart(HttpSession session, Model model) {

        Map<String, Map<Integer,Integer>> cart = (Map<String, Map<Integer,Integer>>) session.getAttribute("prodsession");
        model.addAttribute("cart", cart);
        int sum = 0;
        int no_items = 0;
        for (Map<Integer, Integer> val : cart.values()) {
            Map.Entry<Integer,Integer> entry = val.entrySet().iterator().next();
            sum += entry.getValue() * entry.getKey();
            no_items+= entry.getValue();
        }
        model.addAttribute("sum", sum);
        model.addAttribute("noItems", no_items);
        model.addAttribute("listProducts",productService.getAllTheProducts());
        return "cart";
    }

    @RequestMapping("/delete")
    public String deleteFromCart(@RequestParam("key") String key, HttpSession session, Model model) {

        Map<String, Map<Integer,Integer>> cart = (Map<String, Map<Integer,Integer>>) session.getAttribute("prodsession");
        cart.remove(key);
        int sum = 0;
        for (Map<Integer, Integer> val : cart.values()) {
            Map.Entry<Integer,Integer> entry = val.entrySet().iterator().next();
            sum += entry.getValue() * entry.getKey();
        }
        session.setAttribute("prodsession", cart);
        model.addAttribute("cart", cart);
        model.addAttribute("sum", sum);
        return "redirect:/cart";
    }

    @RequestMapping("/addToCartOne/{id}")
    public String addToCartOne(@PathVariable int id, Model model, HttpSession session) {

        Product p = productService.findById(id);

        Map<String, Map<Integer,Integer>> cart = (Map<String, Map<Integer,Integer>>) session.getAttribute("prodsession");
        Map<Integer,Integer> map = new HashMap<>();

        Map<Integer,Integer> hashMap = cart.get(p.getName());
        Map.Entry<Integer,Integer> entry = hashMap.entrySet().iterator().next();
        map.put(p.getPrice(),entry.getValue() + 1);
        cart.put(p.getName(),map);

        session.setAttribute("prodsession", cart);
        model.addAttribute("cart", cart);
        int sum = 0;
        int no_items = 0;
        for (Map<Integer, Integer> val : cart.values()) {
            Map.Entry<Integer,Integer> entry2 = val.entrySet().iterator().next();
            sum += entry2.getValue() * entry2.getKey();
            no_items+= entry2.getValue();
        }
        model.addAttribute("sum", sum);
        model.addAttribute("noItems", no_items);

        return "redirect:/cart";
    }

    @RequestMapping("/deleteOne/{id}")
    public String deleteOne(@PathVariable int id, Model model, HttpSession session) {

        Product p = productService.findById(id);

        Map<String, Map<Integer,Integer>> cart = (Map<String, Map<Integer,Integer>>) session.getAttribute("prodsession");
        Map<Integer,Integer> map = new HashMap<>();

        Map<Integer,Integer> hashMap = cart.get(p.getName());
        Map.Entry<Integer,Integer> entry = hashMap.entrySet().iterator().next();

        if(entry.getValue()==1)
        {
            cart.remove(p.getName());
        }
        else {
            map.put(p.getPrice(), entry.getValue() - 1);
            cart.put(p.getName(), map);
        }

        session.setAttribute("prodsession", cart);
        model.addAttribute("cart", cart);
        int sum = 0;
        int no_items = 0;
        for (Map<Integer, Integer> val : cart.values()) {
            Map.Entry<Integer,Integer> entry2 = val.entrySet().iterator().next();
            sum += entry2.getValue() * entry2.getKey();
            no_items+= entry2.getValue();
        }
        model.addAttribute("sum", sum);
        model.addAttribute("noItems", no_items);

        return "redirect:/cart";
    }
}
