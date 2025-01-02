package org.koreait.dl.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@ActiveProfiles({"default", "dl"})
public class SentimentServiceTest {

    @Autowired
    private SentimentService service;

    @Test
    void predictTest() {
        List<String> items = List.of("재미없음", "재미있음, 10점 만점");

        double[] predictions = service.predict(items);
        System.out.println(Arrays.toString(predictions));
    }
}
