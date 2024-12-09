package org.koreait.file.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ThumbnailServiceTest {

    @Autowired
    private ThumbnailService service;

    @Test
    void thumbPathTest() {
        String path = service.getThumbPath(1054L, null, 100, 100);
        System.out.println(path);

        String path2 = service.getThumbPath(0L, "https://mimgnews.pstatic.net/image/origin/138/2024/12/09/2187658.jpg", 100, 100);
        System.out.println(path2);
    }
}
