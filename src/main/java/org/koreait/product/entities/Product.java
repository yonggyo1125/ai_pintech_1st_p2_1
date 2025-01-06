package org.koreait.product.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.koreait.global.entities.BaseMemberEntity;
import org.koreait.product.constants.DiscountType;

@Data
@Entity
public class Product extends BaseMemberEntity {
    @Id @GeneratedValue
    private Long seq;

    @Column(length=45, nullable = false)
    private String gid;

    @Column(length=150, nullable = false)
    private String name; // 상품명

    private String summary; // 상품 요약 설명

    @Lob
    private String description; // 상품 상세 설명

    @Enumerated(EnumType.STRING)
    @Column(length=10)
    private DiscountType discountType; // 할인 종류

    private double discount;

    private int maxDiscount; // 최대 할인 금액

    private double pointRate; // 적립률
    private int maxPoint; // 최대 적립금
}
