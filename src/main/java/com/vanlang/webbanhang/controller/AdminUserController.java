package com.vanlang.webbanhang.controller;

import com.vanlang.webbanhang.model.User;
import com.vanlang.webbanhang.service.RoleService;
import com.vanlang.webbanhang.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {
    private final UserService userService;
    private final RoleService roleService;

    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin/users";
    }

    @GetMapping("/{id}")
    public String userDetails(@PathVariable String id, Model model) {
        User user = userService.getUserById(Long.valueOf(id));
        model.addAttribute("user", user);
        return "admin/user-details";
    }

    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable String id, Model model) {
        User user = userService.getUserById(Long.valueOf(id));
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.getAllRoles());
        model.addAttribute("users", userService.getAllUsers());
        return "admin/user-edit";
    }

    @PostMapping("/update")
    public String updateUser(@Valid @ModelAttribute User user,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Please correct the errors in the form.");
            return "redirect:/admin/users/edit/" + user.getId();
        }

        try {
            userService.save(user);
            redirectAttributes.addFlashAttribute("success", "User updated successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "An error occurred while updating the user.");
        }
        return "redirect:/admin/users";
    }
}