package org.koreait.dl.services;

import org.springframework.stereotype.Service;

@Service
public class TrainService {

    public void process() {
        try {
            ProcessBuilder builder = new ProcessBuilder("C:\\Users\\admin\\AppData\\Local\\Programs\\Python\\Python39\\python.exe", "D:/recommend/train.py", "http://localhost:3000/api/dl/data");
            Process process = builder.start();
            int exitCode = process.waitFor();

        } catch (Exception e) {}
    }
}
