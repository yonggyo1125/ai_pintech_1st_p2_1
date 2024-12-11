package org.koreait.dl.tests;

import org.junit.jupiter.api.Test;
import org.koreait.dl.services.PredictService;
import org.koreait.dl.services.TrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@ActiveProfiles({"default", "test", "dl"})
public class PythonRunTest {

    @Autowired
    private PredictService service;

    @Autowired
    private TrainService trainService;

    @Test
    void test1() throws Exception {
        ProcessBuilder builder = new ProcessBuilder("C:\\Users\\admin\\AppData\\Local\\Programs\\Python\\Python39\\python.exe", "D:/recommend/train.py", "http://localhost:3000/api/dl/data");
        Process process = builder.start();
        BufferedReader reader = process.inputReader();
        reader.lines().forEach(System.out::println);
        int exitCode = process.waitFor();
        System.out.println(exitCode);
    }

    @Test
    void test2() throws Exception {
        //python predict.py "[[ 1.23151481,  0.88790998,  1.6140196,   0.94127238,  1.6784415,   1.38504672, -1.57161094, -0.65513703,  0.99961796 , -0.80484811]]"

        ProcessBuilder builder = new ProcessBuilder("C:\\Users\\admin\\AppData\\Local\\Programs\\Python\\Python39\\python.exe", "D:/recommend/predict.py", "[[ 1.23151481,  0.88790998,  1.6140196,   0.94127238,  1.6784415,   1.38504672, -1.57161094, -0.65513703,  0.99961796 , -0.80484811]]");
        Process process = builder.start();
        BufferedReader reader = process.inputReader();
        reader.lines().forEach(System.out::println);
        int exitCode = process.waitFor();
        System.out.println(exitCode);
    }

    @Test
    void test3() {
       // python predict.py "[[ 123151481,  88790998,  16140196,   94127238,  16784415,   138504672, -157161094, -65513703,  99961796 , -80484811]]"

        //trainService.process();

       int[] item =  { 123151481,  88790998,  16140196,   94127238,  16784415,   138504672, -157161094, -65513703,  99961796 , -80484811};
        List<int[]> items = List.of(item);

       int[] predictions = service.predict(items);
       System.out.println(Arrays.toString(predictions));
    }
}
