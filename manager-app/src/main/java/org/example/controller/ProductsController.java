package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.client.BadRequestException;
import org.example.client.ProductsRestClient;
import org.example.controller.payload.NewProductPayload;
import org.example.entity.Product;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("catalogue/products")
public class ProductsController {
    private final ProductsRestClient productsRestClient;

    @GetMapping("list")
    public String getProductList(Model model) {
        model.addAttribute("products", productsRestClient.findAllProducts());
        return "catalogue/products/list";
    }

    @GetMapping("create")
    public String getNewProductPage() {
        return "catalogue/products/new_product";
    }

    @PostMapping("create")
    public String createProduct(NewProductPayload payload,
                                Model model) {
            try {
                Product product = productsRestClient.createProduct(payload.title(), payload.details());
                return "redirect:/catalogue/products/%d".formatted(product.id());
            } catch (BadRequestException e) {
                model.addAttribute("payload", payload);
                model.addAttribute("errors", e.getErrors());

                return "catalogue/products/new_product";
            }

    }
}
