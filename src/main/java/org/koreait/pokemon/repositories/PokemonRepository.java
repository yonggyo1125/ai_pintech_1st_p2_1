package org.koreait.pokemon.repositories;

import org.koreait.pokemon.entities.Pokemon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface PokemonRepository extends JpaRepository<Pokemon, Long>, QuerydslPredicateExecutor<Pokemon> {
}
