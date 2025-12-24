package com.boda.onlineqandaspring.controller;

import com.boda.onlineqandaspring.model.User;
import com.boda.onlineqandaspring.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {

        User user = userService.getUserByUsername(username);

        if (user == null) {
            model.addAttribute("msg", "用户不存在");
            return "login";
        }

        if (!user.getPassword().equals(password)) {
            model.addAttribute("msg", "密码错误");
            return "login";
        }

        // 密码正确，跳转到验证码页面验证
        model.addAttribute("username", username);
        return "captcha";
    }


    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String confirmPassword,
                           Model model,
                           RedirectAttributes redirectAttributes) {

        if (!password.equals(confirmPassword)) {
            model.addAttribute("msg", "两次密码输入不一致");
            return "register";
        }

        if (userService.getUserByUsername(username) != null) {
            model.addAttribute("msg", "用户名已存在");
            return "register";
        }

        userService.registerUser(username, password);

        // 使用 RedirectAttributes 传递消息到登录页面
        redirectAttributes.addFlashAttribute("msg", "注册成功，请登录");
        redirectAttributes.addFlashAttribute("username", username);

        return "redirect:/user/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/user/login";
    }
}