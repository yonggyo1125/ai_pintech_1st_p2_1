package org.koreait.pokemon.tests;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@SpringBootTest
public class ApiTest1 {

    @Test
    void requestTest1() {
        String url = "https://pokeapi.co/api/v2/pokemon";

        RestTemplate tpl = new RestTemplate();

        String response = tpl.getForObject(URI.create(url), String.class);

        System.out.println(response);
    }
}
