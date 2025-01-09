package org.koreait.board.entities;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class BoardView {
    private Long seq; // 게시글 번호
}
