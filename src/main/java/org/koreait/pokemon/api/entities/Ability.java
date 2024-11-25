package org.koreait.pokemon.api.entities;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Ability {
    private UrlItem ability;
    @JsonAlias("is_hidden")
    private boolean isHidden;
    private int slot;
}
