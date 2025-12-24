package com.boda.onlineqandaspring.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

@Service
public class CaptchaService {

    @Autowired
    private ResourceLoader resourceLoader;

    public BufferedImage generateCaptchaImage(HttpSession session) throws IOException {
        int width = 200;
        int height = 100;
        Random r = new Random();

        // 生成验证码
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            sb.append(r.nextInt(10));
        }
        String num = sb.toString();
        session.setAttribute("CaptchaNum", num);

        // 加载背景图片 - 使用 ResourceLoader
        Resource bgResource = resourceLoader.getResource("classpath:static/bg.jpg");
        BufferedImage bgImg = ImageIO.read(bgResource.getInputStream());

        // 缩放背景图片到指定大小
        Image scaled = bgImg.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // 绘制背景和验证码文字
        Graphics2D g = img.createGraphics();
        g.drawImage(scaled, 0, 0, null);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 40));

        // 随机绘制验证码数字
        for (int i = 0; i < num.length(); i++) {
            String ch = String.valueOf(num.charAt(i));
            int x = 30 + i * 40 + r.nextInt(5);
            int y = 50 + r.nextInt(10);
            g.drawString(ch, x, y);
        }
        g.dispose();

        return img;
    }
}
