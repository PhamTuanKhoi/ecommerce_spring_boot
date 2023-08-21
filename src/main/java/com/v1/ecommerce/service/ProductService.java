package com.v1.ecommerce.service;

import com.v1.ecommerce.exception.ProductException;
import com.v1.ecommerce.model.Product;
import com.v1.ecommerce.request.CreateProductRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    public Product create(CreateProductRequest req);

    public Page<Product> findAll(String category, List<String> colors, List<String> sizes, Integer minPrice,
                                 Integer maxPrice, Integer minDiscount, String sort, String stock, Integer pageNumber,
                                 Integer pageSize
    );

    public List<Product> findNewArrivals();

    public List<Product> findBestsellers();

    public Product findById(Long id)throws ProductException;

    public Product findByCategoryId(Long categoryId);

    Product update(Long id, Product productRequest) throws ProductException;

    public String delete (Long id) throws ProductException;
}
