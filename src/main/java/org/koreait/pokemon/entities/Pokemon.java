package org.koreait.pokemon.entities;

import lombok.Data;

@Data
public class Pokemon {
    private Long seq;
    private String name; // 포켓몬 한글 이름
    private String nameEn; // 포켓몬 영어 이름
    private int weight;
    private int height;
    private String frontImage;
}
