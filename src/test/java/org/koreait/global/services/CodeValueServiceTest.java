package org.koreait.global.services;

import org.junit.jupiter.api.Test;
import org.koreait.global.entities.SiteConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({"default", "test"})
public class CodeValueServiceTest {

    @Autowired
    private CodeValueService service;

    @Test
    void saveTest() {
        SiteConfig item = new SiteConfig();
        item.setSiteTitle("제목");
        item.setDescription("설명");
        item.setKeywords("키워드1,키워드2");

        service.save("siteConfig", item);


        SiteConfig item2 = service.get("siteConfig", SiteConfig.class);
        System.out.println(item2);
    }
}
