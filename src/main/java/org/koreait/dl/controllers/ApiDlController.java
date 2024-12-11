package org.koreait.dl.controllers;

import lombok.RequiredArgsConstructor;
import org.koreait.dl.entities.TrainItem;
import org.koreait.dl.services.PredictService;
import org.koreait.dl.services.TrainService;
import org.koreait.global.rests.JSONData;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Profile("dl")
@RestController
@RequestMapping("/api/dl")
@RequiredArgsConstructor
public class ApiDlController {

    private final PredictService predictService;
    private final TrainService trainService;

    @GetMapping("/data")
    public List<TrainItem> sendData(@RequestParam(name="mode", required = false) String mode) {
        List<TrainItem> items = trainService.getList(mode != null && mode.equals("ALL"));

        return items;
    }

    @PostMapping("/predict")
    public JSONData predict(@RequestParam("items") List<int[]> items) {

        int[] predictions = predictService.predict(items);

        return new JSONData(predictions);
    }
}
