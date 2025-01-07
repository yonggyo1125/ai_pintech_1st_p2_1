package org.koreait.board.repositories;

import org.koreait.board.entities.Board;
import org.koreait.board.entities.QBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface BoardRepository extends JpaRepository<Board, String>, QuerydslPredicateExecutor<Board> {

    default boolean exists(String bid) {
        QBoard board = QBoard.board;

        return exists(board.bid.eq(bid));
    }
}
