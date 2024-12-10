package org.koreait.dl.tests;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.BufferedReader;

@SpringBootTest
@ActiveProfiles({"default", "test"})
public class PythonRunTest {

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
}
