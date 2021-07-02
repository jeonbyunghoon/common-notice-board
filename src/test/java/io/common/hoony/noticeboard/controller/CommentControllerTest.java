package io.common.hoony.noticeboard.controller;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CommentControllerTest {

    //웹 API를 테스트할 때 사용
    //스프링 MVC 테스트의 시작점이다.
    //이 클래스를 통해 HTTP GET, POST 등에 대한 API 테스를 할 수 있다.
    @Autowired
    private MockMvc mockMvc;

    private final String url = "/comments";


    @Test
    public void hello_return() throws Exception {
        String hello = "hello";
        mockMvc.perform(get(url + "/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string(hello));

    }
}
