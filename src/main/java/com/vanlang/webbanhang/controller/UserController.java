package com.vanlang.webbanhang.controller;

import com.vanlang.webbanhang.model.User;
import com.vanlang.webbanhang.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.security.Principal;

@Controller // Đánh dấu lớp này là một Controller trong Spring MVC.
@RequestMapping("/")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "users/login";
    }

    @GetMapping("/register")
    public String register(@NotNull Model model) {
        model.addAttribute("user", new User()); // Thêm một đối tượng User mới vào model
        return "users/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") User user,
                           @NotNull BindingResult bindingResult,
                           Model model) {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray(String[]::new);
            model.addAttribute("errors", errors);
            return "users/register";
        }
        try {
            userService.save(user);
            userService.setDefaultRole(user.getUsername());
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred while registering: " + e.getMessage());
            return "users/register";
        }
    }
    @PostMapping("/login")
    public String loginafter(){

        return "redirect:/products";
    }

    @GetMapping("/account")
    public String showAccountPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            // Người dùng chưa đăng nhập, chuyển hướng đến trang đăng nhập
            return "redirect:/login";
        }

        // Người dùng đã đăng nhập
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);
        model.addAttribute("user", user);
        return "users/account";
    }

    @PostMapping("/account/update")
    public String updateAccount(@RequestParam(required = false) String password,
                                @RequestParam String email,
                                @RequestParam String phone,
                                Principal principal,
                                RedirectAttributes redirectAttributes) {
        User user = userService.getUserByUsername(principal.getName());

        if (password != null && !password.isEmpty()) {
            user.setPassword(password);
        }
        if (userService.findByEmail(email).isPresent() && !email.equals(user.getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Email đã được sử dụng.");
            return "redirect:/account";
        }

        if (userService.findbyPhone(email).isPresent() && !email.equals(user.getPhone())) {
            redirectAttributes.addFlashAttribute("error", "Số điện thoại đã được sử dụng.");
            return "redirect:/account";
        }
        user.setPhone(phone);
        user.setEmail(email);
        userService.save(user);

        redirectAttributes.addFlashAttribute("message", "Account updated successfully");
        return "redirect:/account";
    }

}
