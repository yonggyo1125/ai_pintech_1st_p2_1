package org.koreait.file.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.koreait.file.controllers.RequestUpload;
import org.koreait.member.test.annotations.MockMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.Charset;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles({"default", "test"})
@AutoConfigureMockMvc
public class FileUploadServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FileUploadService service;

    @Autowired
    private ObjectMapper om;

    private MockMultipartFile[] files;

    @BeforeEach
    void init() {
        files = new MockMultipartFile[] {
                new MockMultipartFile("file", "test1.png", MediaType.IMAGE_PNG_VALUE, new byte[] {1, 2, 3}),
                new MockMultipartFile("file", "test2.png", MediaType.IMAGE_PNG_VALUE, new byte[] {1, 2, 3}),
        };
    }

    @Test
    @DisplayName("파일 업로드 기능 테스트")
    void uploadTest() {
        RequestUpload form = new RequestUpload();
        form.setGid(UUID.randomUUID().toString());
        form.setFiles(files);

        assertDoesNotThrow(() -> service.upload(form));
    }
    
    @Test
    @DisplayName("파일 업로드 통합 테스트")
    @MockMember
    void uploadControllerTest() throws Exception {
        String body = mockMvc.perform(multipart("/api/file/upload")
                .file(files[0])
                .file(files[1])
                .param("gid", UUID.randomUUID().toString())
                        .with(csrf().asHeader()))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString(Charset.forName("UTF-8"));

        System.out.println(body);

        /*
        List<FileInfo> items = om.readValue(body, new TypeReference<>() {});
        boolean result1 = items.stream().anyMatch(i -> i.getFileName().equals(files[0].getOriginalFilename()));
        boolean result2 = items.stream().anyMatch(i -> i.getFileName().equals(files[1].getOriginalFilename()));
        */
        //assertTrue(result1 && result2);

    }
}
