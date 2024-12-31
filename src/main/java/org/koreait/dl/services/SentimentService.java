package org.koreait.dl.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("dl")
public class SentimentService {

    @Value("${python.run.path}")
    private String runPath;

    @Value("${python.script2.path}")
    private String scriptPath;
}
