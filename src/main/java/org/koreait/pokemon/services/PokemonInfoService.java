package org.koreait.pokemon.services;

import com.querydsl.core.BooleanBuilder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.koreait.global.libs.Utils;
import org.koreait.global.paging.ListData;
import org.koreait.global.paging.Pagination;
import org.koreait.pokemon.controllers.PokemonSearch;
import org.koreait.pokemon.entities.Pokemon;
import org.koreait.pokemon.entities.QPokemon;
import org.koreait.pokemon.exceptions.PokemonNotFoundException;
import org.koreait.pokemon.repositories.PokemonRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

import static org.springframework.data.domain.Sort.Order.desc;

@Lazy
@Service
@RequiredArgsConstructor
public class PokemonInfoService {

    private final PokemonRepository pokemonRepository;
    private final HttpServletRequest request;
    private final Utils utils;

    /**
     * 포켓몬 목록 조회
     *
     * @param search
     * @return
     */
    public ListData<Pokemon> getList(PokemonSearch search) {
        int page = Math.max(search.getPage(), 1); // 페이지 번호
        int limit = search.getLimit(); // 한페이지 당 레코드 갯수
        limit = limit < 1 ? 20 : limit;

        QPokemon pokemon = QPokemon.pokemon;

        /* 검색 처리 S */
        BooleanBuilder andBuilder = new BooleanBuilder();
        String skey = search.getSkey();
        if (StringUtils.hasText(skey)) { // 키워드 검색
            andBuilder.and(pokemon.name
                    .concat(pokemon.nameEn)
                    .concat(pokemon.flavorText).contains(skey));
        }
        /* 검색 처리 E */

        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(desc("seq")));

        Page<Pokemon> data = pokemonRepository.findAll(andBuilder, pageable);
        List<Pokemon> items = data.getContent(); // 조회된 목록

        // 추가 정보 처리
        items.forEach(this::addInfo);

        int ranges = utils.isMobile() ? 5 : 10;
        Pagination pagination = new Pagination(page, (int)data.getTotalElements(), ranges, limit, request);

        return new ListData<>(items, pagination);
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
