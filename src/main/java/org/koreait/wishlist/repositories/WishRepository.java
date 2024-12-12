package org.koreait.wishlist.repositories;

import org.koreait.wishlist.entities.Wish;
import org.koreait.wishlist.entities.WishId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface WishRepository extends JpaRepository<Wish, WishId>, QuerydslPredicateExecutor<Wish> {

}
