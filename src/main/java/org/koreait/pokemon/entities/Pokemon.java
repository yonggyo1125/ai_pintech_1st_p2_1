package org.koreait.pokemon.entities;

import lombok.Data;

@Data
public class Pokemon {
    private Long seq;
    private String name; // 포켓몬 한글 이름
    private String nameEn; // 포켓몬 영어 이름
    private int weight;
    private int height;
    private int baseExperience;
    private String frontImage;
    private String flavorText; // 설명
    private String types; // 타입1||타입2||타입3
    private String abilities; // 능력1||능력2||능력3
}
