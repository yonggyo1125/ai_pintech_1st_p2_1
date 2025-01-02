package org.koreait.dl.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@ActiveProfiles({"default", "dl"})
@AutoConfigureMockMvc
public class ApiDlControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void sentimentTest() {

    }
}
