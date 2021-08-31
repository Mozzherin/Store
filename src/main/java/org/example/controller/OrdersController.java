package org.example.controller;

import org.example.entityes.Order;
import org.example.entityes.Product;
import org.example.entityes.User;
import org.example.service.OrderService;
import org.example.service.ProductService;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class OrdersController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;

    @GetMapping("/order")
    public String getOrderList(Model model) {
        model.addAttribute("orders", orderService.findAll());
        return "orders";
    }

    @PostMapping("/order/{id}")
    public String createNewOrder(@AuthenticationPrincipal User user,
                           @ModelAttribute("product") Product product,
                           Model model) {

        orderService.save(new Order(user.getId(), product.getId()));
        return "redirect:/main";
    }

    @GetMapping("/order/{id}")
    public String orderDetails(@ModelAttribute("order") Order order,
                               Model model) {
        model.addAttribute("order", orderService.findById(order.getId()));
        model.addAttribute("product", productService.findById(orderService.findById(order.getId()).getProductId()));
        model.addAttribute("user", userService.findById(orderService.findById(order.getId()).getUserId()));

        return "orderEdit";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/order/{id}")
    public String deleteOrder(@PathVariable Long id, Model model) {
        orderService.delete(id);
        model.addAttribute("orders", orderService.findAll());
        return "orders";
    }
}
