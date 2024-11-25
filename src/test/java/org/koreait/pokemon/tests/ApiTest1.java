package org.koreait.pokemon.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.koreait.pokemon.api.entities.ApiPokemon;
import org.koreait.pokemon.api.entities.ApiResponse;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@SpringBootTest
public class ApiTest1 {

    private RestTemplate tpl;

    @BeforeEach
    void init() {
        tpl = new RestTemplate();
    }

    @Test
    void requestTest1() {
        String url = "https://pokeapi.co/api/v2/pokemon";

        ApiResponse response = tpl.getForObject(URI.create(url), ApiResponse.class);

        System.out.println(response);
    }

    @Test
    void requestTest2() {
        String url = "https://pokeapi.co/api/v2/pokemon/1";

        ApiPokemon response = tpl.getForObject(URI.create(url), ApiPokemon.class);
        System.out.println(response);
    }

    @Test
    void requestTest3() {
        String url = "https://pokeapi.co/api/v2/pokemon-species/1";
        ApiPokemon response = tpl.getForObject(URI.create(url), ApiPokemon.class);
        System.out.println(response);
    }
}
