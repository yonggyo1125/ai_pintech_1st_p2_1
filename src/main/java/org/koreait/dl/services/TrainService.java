package org.koreait.dl.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TrainService {

    @Value("${python.run.path}")
    private String runPath;

    @Value("${python.script.path}")
    private String scriptPath;

    @Value("${python.data.url}")
    private String dataUrl;

    public void process() {
        try {
            ProcessBuilder builder = new ProcessBuilder(runPath, scriptPath + "train.py", dataUrl);
            Process process = builder.start();
            int exitCode = process.waitFor();

        } catch (Exception e) {}
    }
}
