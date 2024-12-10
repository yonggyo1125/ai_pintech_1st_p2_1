package org.koreait.pokemon.services;

import lombok.RequiredArgsConstructor;
import org.koreait.global.paging.ListData;
import org.koreait.pokemon.controllers.PokemonSearch;
import org.koreait.pokemon.entities.Pokemon;
import org.koreait.pokemon.exceptions.PokemonNotFoundException;
import org.koreait.pokemon.repositories.PokemonRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Lazy
@Service
@RequiredArgsConstructor
public class PokemonInfoService {

    private final PokemonRepository pokemonRepository;

    /**
     * 포켓몬 목록 조회
     *
     * @param search
     * @return
     */
    public ListData<Pokemon> getList(PokemonSearch search) {

        return null;
    }

    /**
     * 포켓몬 단일 조회
     *
     * @param seq
     * @return
     */
    public Pokemon get(Long seq) {

        Pokemon item = pokemonRepository.findById(seq).orElseThrow(PokemonNotFoundException::new);

        // 추가 정보 처리
        addInfo(item);

        return item;
    }

    /**
     * 추가 정보 처리
     *
     * @param item
     */
    private void addInfo(Pokemon item) {

    }
}
