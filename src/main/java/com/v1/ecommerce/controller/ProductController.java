package com.v1.ecommerce.controller;

import com.v1.ecommerce.exception.ProductException;
import com.v1.ecommerce.model.Product;
import com.v1.ecommerce.request.CreateProductRequest;
import com.v1.ecommerce.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@Tag(name = "Product")
public class ProductController {
    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @SecurityRequirement(name = "bearer-key")
    @Parameter(hidden = true)
    @GetMapping
    public ResponseEntity<Page<Product>> findAll(
            @RequestParam() String category, @RequestParam() List<String> colors,
            @RequestParam() List<String> sizes, @RequestParam() Integer minPrice,
            @RequestParam() Integer maxPrice, @RequestParam() Integer minDiscount,
            @RequestParam() String sort, @RequestParam() String stock,
            @RequestParam() Integer pageNumber, @RequestParam() Integer pageSize
    ) {
        Page<Product> res = this.productService.findAll(category, colors, sizes, minPrice,
                maxPrice, minDiscount, sort, stock, pageNumber, pageSize);

        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }


    @GetMapping("/new")
    public List<Product> findNewArrivals(){
        return this.productService.findNewArrivals();
    };

    @SecurityRequirement(name = "bearer-key")
    @Operation(
            description = "get product bestsellers",
            summary = "get product order many",
            responses = {
                    @ApiResponse(
                            description = "success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "UnAuthorized / Invalid token",
                            responseCode = "403"
                    )
            }
    )
    @GetMapping("/bestsellers")
    public List<Product> findBestsellers(){
        return this.productService.findBestsellers();
    };

    @GetMapping("/{id}")
    public Product findById(@PathVariable Long id) throws ProductException {

        System.out.println("id = " + id);
        return this.productService.findById(id);
    }

    @GetMapping("category/{categoryId}")
    public Product findByCategoryId(@PathVariable Long categoryId){
        return this.productService.findByCategoryId(categoryId);
    };

    @PostMapping
    public Product create(@RequestBody CreateProductRequest req){
        return this.productService.create((req));
    };

    @PatchMapping("/{id}")
    public Product update(@PathVariable Long id,@RequestBody Product productRequest) throws ProductException {
        return this.productService.update(id, productRequest);
    };

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) throws ProductException {
        return this.productService.delete(id);
    };
}
