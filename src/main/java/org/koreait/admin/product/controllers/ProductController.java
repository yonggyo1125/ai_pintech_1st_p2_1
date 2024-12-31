package org.koreait.admin.product.controllers;

import lombok.RequiredArgsConstructor;
import org.koreait.admin.global.menu.SubMenus;
import org.koreait.global.annotations.ApplyErrorPage;
import org.koreait.global.libs.Utils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@ApplyErrorPage
@RequiredArgsConstructor
@Controller("adminProductController")
@RequestMapping("/admin/product")
public class ProductController implements SubMenus {

    private final Utils utils;

    @Override
    @ModelAttribute("menuCode")
    public String menuCode() {
        return "product";
    }

    /**
     * 상품 목록
     *
     * @param model
     * @return
     */
    @GetMapping({"", "/list"})
    public String list(Model model) {
        commonProcess("list", model);

        return "admin/product/list";
    }

    /**
     * 상품 등록
     *
     * @param model
     * @return
     */
    @GetMapping("/add")
    public String add(Model model) {
        commonProcess("product_save", model);


        return "admin/product/add";
    }

    /**
     * 상품 정보 수정
     *
     * @param seq
     * @param model
     * @return
     */
    @GetMapping("/edit/{seq}")
    public String edit(@PathVariable("seq") Long seq, Model model) {
        commonProcess("product_save", model);

        return "admin/product/edit";
    }

    /**
     * 상품 등록, 수정 처리
     *
     * @return
     */
    @PostMapping("/save")
    public String save(Model model) {
        commonProcess("product_save", model);

        return "redirect:/admin/product/list";
    }

    /**
     * 상품 분류 목록
     *
     * @return
     */
    @GetMapping("/category")
    public String categoryList(Model model) {
        commonProcess("category_list", model);

        return "admin/product/category/list";
    }

    /**
     * 분류 등록
     *
     * @return
     */
    @GetMapping({"/category/add", "/category/edit/{cate}"})
    public String categoryUpdate(@PathVariable(name="cate", required = false) String cate, Model model) {
        commonProcess("category_save", model);

        return "admin/product/category/add";
    }

    /**
     * 분류 등록, 수정 처리
     *
     * @param model
     * @return
     */
    @PostMapping("/category/save")
    public String categorySave(Model model) {
        commonProcess("category_save", model);

        return "redirect:/admin/product/category";
    }

    /**
     * 배송 정책 관리
     *
     * @param model
     * @return
     */
    @GetMapping("/delivery")
    public String delivery(Model model) {
        commonProcess("delivery", model);

        return "admin/product/delivery/list";
    }


    /**
     * 공통 처리 부분
     *
     * @param mode
     * @param model
     */
    private void commonProcess(String mode, Model model) {

        model.addAttribute("subMenuCode", mode);
    }
}
