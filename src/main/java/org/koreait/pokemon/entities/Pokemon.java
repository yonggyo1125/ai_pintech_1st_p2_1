package org.koreait.pokemon.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.koreait.global.entities.BaseEntity;

import java.util.List;

@Data
@Entity
public class Pokemon extends BaseEntity {
    @Id
    private Long seq;

    @Column(length=50)
    private String name; // 포켓몬 한글 이름

    @Column(length=50)
    private String nameEn; // 포켓몬 영어 이름
    private int weight;
    private int height;
    private int baseExperience;

    private String frontImage;

    @Lob
    private String flavorText; // 설명
    private String types; // 타입1||타입2||타입3
    private String abilities; // 능력1||능력2||능력3

    @Transient
    private List<String> _types;

    @Transient
    private List<String> _abilities;
}
