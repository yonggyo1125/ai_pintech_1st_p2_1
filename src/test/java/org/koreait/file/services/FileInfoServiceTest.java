package org.koreait.file.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.koreait.file.exceptions.FileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles({"default", "test"})
@DisplayName("파일 정보 조회 기능 테스트")
public class FileInfoServiceTest {
    
    @Autowired
    private FileInfoService infoService;
    
    @Test
    @DisplayName("없는 파일 번호로 조회시에 FileNotFoundException 발생하는지 테스트")
    void notExistsFileInfoTest() {
        // 테스트 성공시 발생된 예외가 반환
        FileNotFoundException thrown = assertThrows(FileNotFoundException.class, () -> {
            infoService.get(99999L);
        });

        String message = thrown.getMessage();
        HttpStatus status = thrown.getStatus();

        assertEquals("NotFound.file", message);
        assertSame(HttpStatus.NOT_FOUND, status);
    }
}
