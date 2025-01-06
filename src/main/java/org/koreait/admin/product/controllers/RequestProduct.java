package org.koreait.admin.product.controllers;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.koreait.file.entities.FileInfo;
import org.koreait.product.constants.DiscountType;

import java.util.List;

@Data
public class RequestProduct {
    private String mode;
    private Long seq; // 상품 번호, 수정시 필요

    private boolean open; // true : 소비자페이지 상품 노출

    @NotBlank
    private String gid;

    @NotBlank
    private String name; // 상품명
    private String summary; // 상품 요약 설명 
    private String description; // 상품 상세 설명

    private int consumerPrice; // 소비자가
    private int salePrice; // 판매가

    private DiscountType discountType; // 할인 종류
    private double discount; // 정가할인 금액(1000), 할인율(10.5%)
    private int maxDiscount; // 최대 할인 금액

    private double pointRate; // 적립률 - 결제 금액의 상품의 판매가
    private int maxPoint; // 최대 적립금

    private List<FileInfo> mainImages; // 상품 상세 메인이미지

    private List<FileInfo> listImages; // 목록 이미지

    private List<FileInfo> editorImages; // 상세설명 이미지
}
