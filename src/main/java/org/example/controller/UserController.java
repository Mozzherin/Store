package org.example.controller;

import org.example.entityes.User;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginUser(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @GetMapping("/registration")
    public String registrationUser(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@ModelAttribute("user") @Valid User user,
                          BindingResult bindingResult,
                          @RequestParam("file") MultipartFile file) {
        if (bindingResult.hasErrors()) {
            return "registration";
        }
        userService.sendEmail(user, file);
        return "redirect:/login";
    }

    @GetMapping("/activate/{code}")
    public String activate(@PathVariable String code) {
        userService.activateUser(code);
        return "redirect:/login";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/user")
    public String getUserList(Model model) {
        model.addAttribute("users", userService.findAll());
        return "users";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/user/{id}")
    public String userEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("user", userService.findById(id));
        System.out.println("OPEN " + userService.findById(id));
        return "userEdit";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/user/{id}")
    public String update(@ModelAttribute("user") @Valid User user,
                         @RequestParam("file") MultipartFile file,
                         Model model) {
        System.out.println(user);
        userService.update(user, file);
        model.addAttribute(userService.findAll());
        return "redirect:/user";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/user/{id}")
    public String delete(@PathVariable Long id, Model model) {
        userService.delete(id);
        model.addAttribute(userService.findAll());
        return "redirect:/user";
    }
}
