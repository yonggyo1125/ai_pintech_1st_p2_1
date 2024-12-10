package org.koreait.dl.controllers;

import lombok.RequiredArgsConstructor;
import org.koreait.dl.entities.TrainItem;
import org.koreait.dl.services.PredictService;
import org.koreait.global.rests.JSONData;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@Profile("dl")
@RestController
@RequestMapping("/api/dl")
@RequiredArgsConstructor
public class ApiDlController {

    private final PredictService predictService;

    @GetMapping("/data")
    public List<TrainItem> sendData() {
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
                        .result(random.nextInt(4))
                        .build()
                ).toList();
        return items;
    }

    @PostMapping("/predict")
    public JSONData predict(@RequestParam("items") List<int[]> items) {

        int[] predictions = predictService.predict(items);

        return new JSONData(items);
    }
}
