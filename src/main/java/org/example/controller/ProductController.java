package org.example.controller;

import org.example.entityes.Product;
import org.example.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/")
    public String greeting(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("products", productService.findAll());
        return "greeting";
    }

    @GetMapping("/main")
    public String main(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("products", productService.findAll());
        return "main";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/main")
    public String addProduct(@ModelAttribute("product") @Valid Product product,
                             BindingResult bindingResult,
                             @RequestParam("file") MultipartFile file,
                             Model model) {
        if (bindingResult.hasErrors()) {
            return "main";
        }
        productService.addNewProduct(product, file);
        model.addAttribute("product", new Product());
        model.addAttribute("products", productService.findAll());
        return "main";
    }

    @GetMapping("/product/{id}")
    public String productEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.findById(id));
        return "productEdit";
    }

    @PostMapping("filter")
    public String filter(@RequestParam String category,
                         @RequestParam String manufacturer,
                         @RequestParam String model,
                         Model modelView) {
        modelView.addAttribute("products", productService.findProduct(category, manufacturer, model));
        modelView.addAttribute("product", new Product());
        return "main";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/product/{id}")
    public String update(@ModelAttribute("product") Product product,
                         @RequestParam("file") MultipartFile file,
                         Model model) {
        productService.update(product, file);
        model.addAttribute(productService.findAll());
        return "redirect:/main";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/product/{id}")
    public String delete(@PathVariable Long id, Model model) {
        productService.delete(id);
        model.addAttribute(productService.findAll());
        return "redirect:/main";
    }
}