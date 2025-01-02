package org.koreait.dl.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@ActiveProfiles({"default", "dl"})
@AutoConfigureMockMvc
public class ApiDlControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void sentimentTest() throws Exception {

        mockMvc.perform(post("/api/dl/sentiment")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("items", "재미없음", "재미있음")
                        .with(csrf().asHeader())
                )
                .andDo(print());
    }
}
