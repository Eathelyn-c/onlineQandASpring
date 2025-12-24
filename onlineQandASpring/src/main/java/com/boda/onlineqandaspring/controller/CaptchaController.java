package com.boda.onlineqandaspring.controller;

import com.boda.onlineqandaspring.model.User;
import com.boda.onlineqandaspring.service.CaptchaService;
import com.boda.onlineqandaspring.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Controller
@RequestMapping("/captcha")
public class CaptchaController {

    @Autowired
    private CaptchaService captchaService;

    @Autowired
    private UserService userService;

    @GetMapping("/image")
    public void generateCaptcha(HttpSession session, HttpServletResponse response)
            throws IOException {
        response.setContentType("image/png");

        BufferedImage captchaImage = captchaService.generateCaptchaImage(session);
        ImageIO.write(captchaImage, "png", response.getOutputStream());
    }

    @PostMapping("/check")
    public String checkCaptcha(@RequestParam String code,
                               @RequestParam String username,
                               HttpSession session,
                               Model model) {
        String correctCode = (String) session.getAttribute("CaptchaNum");

        if (correctCode != null && correctCode.equalsIgnoreCase(code)) {
            // 验证码验证成功，获取用户信息并设置 session
            User user = userService.getUserByUsername(username);

            if (user != null) {
                session.removeAttribute("CaptchaNum");
                session.setAttribute("userID", user.getUserID());
                session.setAttribute("loginUsername", user.getUsername());
                // 重定向到话题列表
                return "redirect:/topic/list";
            } else {
                model.addAttribute("msg", "用户信息异常，请重新登录");
                model.addAttribute("username", username);
                return "captcha";
            }
        } else {
            model.addAttribute("msg", "验证码错误，请重新输入");
            model.addAttribute("username", username);
            return "captcha";
        }
    }
}


