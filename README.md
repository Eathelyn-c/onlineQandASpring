# 在线问答平台（Spring Boot + Thymeleaf）

一个基于 Spring Boot 的在线问答示例项目，包含注册、登录、验证码二次验证、话题发布与详情查看、登录拦截等功能。

## 功能特性
- 用户注册与登录
- 登录二次验证（图片验证码）
- 话题列表、详情与发布
- 登录拦截器：未登录访问业务页面将被重定向到登录页
- Thymeleaf 模板渲染，简单的表单校验

## 技术栈
- Java 17
- Spring Boot 3（Web、Thymeleaf）
- Lombok（如使用）
- 构建工具：Maven

## 项目结构
```
src/
 ├─ main/
 │   ├─ java/com/boda/onlineqandaspring/
 │   │   ├─ OnlineQandASpringApplication.java
 │   │   ├─ config/
 │   │   │   └─ WebConfig.java                # 首页重定向、静态资源、拦截器
 │   │   ├─ interceptor/
 │   │   │   └─ LoginInterceptor.java         # 登录拦截（检查 session）
 │   │   ├─ controller/
 │   │   │   ├─ UserController.java           # 注册/登录/登出
 │   │   │   ├─ CaptchaController.java        # 验证码图片与校验
 │   │   │   ├─ TopicController.java          # 话题列表/详情/发布
 │   │   │   └─ DiscussController.java        #（若启用）评论
 │   │   ├─ service/
 │   │   │   ├─ CaptchaService.java           # 使用 bg.jpg 绘制验证码
 │   │   │   ├─ UserService.java
 │   │   │   └─ MessageService.java
 │   │   ├─ model/                            # User、Message
 │   │   └─ repository/                       # UserRepository、MessageRepository
 │   └─ resources/
 │       ├─ application.properties
 │       ├─ static/
 │       │   └─ bg.jpg                        # 验证码背景图
 │       └─ templates/                        # Thymeleaf 视图
 │           ├─ login.html
 │           ├─ register.html
 │           ├─ captcha.html
 │           ├─ online.html
 │           └─ topicDetail.html
```

## 本地运行
1. 安装 Java 17 与 Maven。
2. 在项目根目录运行：
```bash
mvn spring-boot:run
```
3. 访问：
- 登录页：http://localhost:8080/user/login
- 根路径 `/` 已在 `WebConfig` 重定向到 `/user/login`。

## 登录 + 验证码流程
1. 在 `/user/login` 提交用户名与密码（POST `/user/login`）。
2. 用户不存在或密码错误 → 留在登录页并显示错误消息。
3. 密码正确 → 跳转验证码页 `captcha.html`。
4. 验证码图片由接口 `GET /captcha/image` 动态生成，`CaptchaService` 使用 `classpath:static/bg.jpg` 作为背景，验证码字符串存放在 Session 的 `CaptchaNum`。
5. 提交验证码（POST `/captcha/check`）：
   - 正确 → 将 `userID` 与 `loginUsername` 写入 Session，重定向到 `/topic/list`。
   - 错误 → 留在验证码页并提示错误，可刷新图片后重试。

## 主要路由
- 用户
  - `GET /user/login`：登录页
  - `POST /user/login`：提交登录（密码正确进入验证码）
  - `GET /user/register`：注册页
  - `POST /user/register`：提交注册
  - `GET /user/logout`：退出登录（清空 Session）
- 验证码
  - `GET /captcha/image`：验证码图片（PNG）
  - `POST /captcha/check`：验证码校验并登录
- 话题
  - `GET /topic/list`：话题列表（需登录）
  - `POST /topic/create`：发布话题（需登录）
  - `GET /topic/{topicId}`：话题详情（需登录）

## 拦截器与白名单
- 拦截器：`LoginInterceptor` 在 `WebConfig#addInterceptors` 中注册。
- 白名单（无需登录可访问）：
  - `/user/login`, `/user/register`, `/user/logout`
  - `/captcha/**`
  - `/static/**`, `/images/**`, `/Images/**`
  - `/error`
- 其他路径若 Session 无 `userID` → 重定向到 `/user/login`。

## 模板与静态资源
- 模板（`src/main/resources/templates/`）不能通过 URL 直接访问，例如 `templates/login.html`；需通过控制器返回视图名 `return "login";`，浏览器访问 `/user/login`。
- 静态资源位于 `src/main/resources/static/`，验证码图片非静态文件，由 `/captcha/image` 动态生成。
- 验证码背景图加载：`CaptchaService` 使用 `ResourceLoader` 读取 `classpath:static/bg.jpg`，适用于开发与打包运行环境。

## 构建与打包
- 打包可执行 JAR：
```bash
mvn -DskipTests package
```
- 运行 JAR：
```bash
java -jar target/onlineQandASpring-0.0.1-SNAPSHOT.jar
```
