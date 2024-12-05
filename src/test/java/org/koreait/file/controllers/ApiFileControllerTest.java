package org.koreait.file.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
@SpringBootTest
@ActiveProfiles({"default", "test"})
@AutoConfigureMockMvc
public class ApiFileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        //mockMvc = MockMvcBuilders.standaloneSetup(ApiFileController.class).build();
    }

    @Test
    void test1() throws Exception {
        /**
         * MockMultipartFile
         */
        MockMultipartFile file1 = new MockMultipartFile("file", "test1.png", MediaType.IMAGE_PNG_VALUE, new byte[] {1, 2, 3});
        MockMultipartFile file2 = new MockMultipartFile("file", "test2.png", MediaType.IMAGE_PNG_VALUE, new byte[] {1, 2, 3});

        mockMvc.perform(multipart("/api/file/upload")
                        .file(file1)
                        .file(file2)
                        .param("gid", "testgid")
                        .param("location", "testlocation")
                        .with(csrf().asHeader()))
                .andDo(print());
    }
}
