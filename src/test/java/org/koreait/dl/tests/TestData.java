package org.koreait.dl.tests;

import org.junit.jupiter.api.Test;
import org.koreait.dl.entities.TrainItem;
import org.koreait.dl.repositories.TrainItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@SpringBootTest
public class TestData {
    @Autowired
    private TrainItemRepository repository;

    @Test
    public void update() {
        Random random = new Random();
        List<TrainItem> items = IntStream.range(0, 1000)
                .mapToObj(i -> TrainItem.builder()
                        .item1(random.nextInt())
                        .item2(random.nextInt())
                        .item3(random.nextInt())
                        .item4(random.nextInt())
                        .item5(random.nextInt())
                        .item6(random.nextInt())
                        .item7(random.nextInt())
                        .item8(random.nextInt())
                        .item9(random.nextInt())
                        .item10(random.nextInt())
                        .result(random.nextInt(4)) // 0 ~ 4
                        .build()
                ).toList();
        repository.saveAllAndFlush(items);
    }
}
