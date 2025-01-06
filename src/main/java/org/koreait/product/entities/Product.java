package org.koreait.product.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.koreait.file.entities.FileInfo;
import org.koreait.global.entities.BaseMemberEntity;
import org.koreait.product.constants.DiscountType;

import java.util.List;

@Data
@Entity
public class Product extends BaseMemberEntity {
    @Id @GeneratedValue
    private Long seq;

    private boolean open; // 상품 노출 여부

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

    @Transient
    private List<FileInfo> mainImages; // 상품 상세 메인이미지

    @Transient
    private List<FileInfo> listImages; // 목록 이미지

    @Transient
    private List<FileInfo> editorImages; // 상세설명 이미지
}
