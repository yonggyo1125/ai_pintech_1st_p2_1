package org.koreait.pokemon.api.entities;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

import java.util.Map;

@Data
public class SpritesOther {
    @JsonAlias("official-artwork")
    private Map<String, String> officialArtwork;
}
