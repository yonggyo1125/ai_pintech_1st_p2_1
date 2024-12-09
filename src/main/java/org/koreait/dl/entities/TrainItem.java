package org.koreait.dl.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.koreait.global.entities.BaseEntity;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor // 빌더 패턴일때 기본 생성자가 접근 가능해야 하는 경우
public class TrainItem extends BaseEntity {
    @Id @GeneratedValue
    private Long seq;
    private int item1;
    private int item2;
    private int item3;
    private int item4;
    private int item5;
    private int item6;
    private int item7;
    private int item8;
    private int item9;
    private int item10;
    private int result;
}
