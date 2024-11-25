package org.koreait.pokemon.api.services;

import lombok.RequiredArgsConstructor;
import org.koreait.pokemon.api.entities.ApiPokemon;
import org.koreait.pokemon.api.entities.ApiResponse;
import org.koreait.pokemon.api.entities.UrlItem;
import org.koreait.pokemon.entities.Pokemon;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApiUpdateService {

    private final RestTemplate tpl;

    /**
     * 1페이지당 100개씩 DB 반영
     *
     * @param page
     */
    public void update(int page) {
        int limit = 100;
        int offset = (page - 1) * limit; // 시작 레코드 번호, 0, 100, ..
        String url = String.format("https://pokeapi.co/api/v2/pokemon?offset=%d&limit=%d", offset, limit);
        ApiResponse response = tpl.getForObject(URI.create(url), ApiResponse.class);
        List<UrlItem> items = response.getResults();
        if (items == null || items.isEmpty()) { // 조회된 결과가 없는 경우 처리 X
            return;
        }
        /* 상세 정보 처리 S */
        for (UrlItem item : items) {
            Pokemon pokemon = new Pokemon();
            // 기초 데이터
            ApiPokemon data1 = tpl.getForObject(URI.create(item.getUrl()), ApiPokemon.class);
            pokemon.setSeq(Long.valueOf(data1.getId()));
            pokemon.setNameEn(data1.getName()); // 영문 이름
            pokemon.setHeight(data1.getHeight());
            pokemon.setWeight(data1.getWeight());
            pokemon.setBaseExperience(data1.getBaseExperience());
            pokemon.setFrontImage(data1.getSprites().getOther().getOfficialArtwork().get("front_default"));

            // 포켓몬 한글 이름, 포켓몬 한글 설명
            String url2 = String.format("https://pokeapi.co/api/v2/pokemon-species/%d", data1.getId());
            ApiPokemon data2 = tpl.getForObject(URI.create(url2), ApiPokemon.class);
        }
        /* 상세 정보 처리 E */

    }
}
