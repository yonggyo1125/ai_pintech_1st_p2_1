package org.koreait.admin.product.controllers;

import lombok.RequiredArgsConstructor;
import org.koreait.global.annotations.ApplyErrorPage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@ApplyErrorPage
@RequiredArgsConstructor
@Controller("adminProductController")
@RequestMapping("/admin/product")
public class ProductController {
}
