// 文件路径：src\test\java\com\example\demo\controller\HomeControllerTest.java

package com.example.demo.controller;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(HomeController.class)  // 针对 HomeController类 的 Web 测试
public class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;  // 注入 MockMvc实例 用于模拟 MVC 服务及其相关操作（例如发送请求）

    @Test
    public void testHomePage() throws Exception {
        mockMvc.perform(get("/"))  // 发起对 “/” 的 GET 请求
            .andExpect(status().isOk())  // 期望得到 HTTP 200 应答
            .andExpect(view().name("home"))  // 期望得到 home 视图
            .andExpect(content().string(containsString("Welcome to...")));  // 期望包含某些字符串
    }
}
