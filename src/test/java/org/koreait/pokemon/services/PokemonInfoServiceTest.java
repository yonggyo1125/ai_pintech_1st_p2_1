package org.koreait.pokemon.services;

import org.junit.jupiter.api.Test;
import org.koreait.global.paging.ListData;
import org.koreait.pokemon.controllers.PokemonSearch;
import org.koreait.pokemon.entities.Pokemon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PokemonInfoServiceTest {
    @Autowired
    private PokemonInfoService infoService;

    @Test
    void test1() {
        //Pokemon item = infoService.get(1L);
        //System.out.println(item);
        PokemonSearch search = new PokemonSearch();
        search.setSkey("나무줄기");
        ListData<Pokemon> items = infoService.getList(search);
        items.getItems().forEach(System.out::println);
    }
}
