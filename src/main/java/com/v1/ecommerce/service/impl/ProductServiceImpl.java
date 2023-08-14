package com.v1.ecommerce.service.impl;

import com.v1.ecommerce.exception.ProductException;
import com.v1.ecommerce.model.Category;
import com.v1.ecommerce.model.Product;
import com.v1.ecommerce.repository.CategoryRepository;
import com.v1.ecommerce.repository.ProductRepository;
import com.v1.ecommerce.request.CreateProductRequest;
import com.v1.ecommerce.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    private ModelMapper modelMapper;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Product create(CreateProductRequest req) {
        System.out.println("req.getTopLevelCategory() = " + req.getTopLevelCategory());
        Category topLevelCategory = this.categoryRepository.findByName(req.getTopLevelCategory());

        if(topLevelCategory == null){
            Category category = new Category();
            category.setName(req.getTopLevelCategory());
            category.setLevel(1);

            topLevelCategory = this.categoryRepository.save(category);
        }

        System.out.println("topLevelCategory = " + topLevelCategory);

        Category secondLevelCategory = this.categoryRepository.findByName(req.getSecondLevelCategory());

        if(secondLevelCategory == null){
            Category category = new Category();
            category.setName(req.getSecondLevelCategory());
            category.setParentCategory(topLevelCategory);
            category.setLevel(2);

            secondLevelCategory = this.categoryRepository.save(category);
        }

        Category thirdLevelCategory = this.categoryRepository.findByName(req.getThirdLevelCategory());

        if(thirdLevelCategory == null){
            Category category = new Category();
            category.setName(req.getThirdLevelCategory());
            category.setParentCategory(secondLevelCategory);
            category.setLevel(3);

            thirdLevelCategory = this.categoryRepository.save(category);
        }

        Product product = modelMapper.map(req, Product.class);
        product.setCategory(thirdLevelCategory);
        product.setCreatedAt(LocalDateTime.now());

        return this.productRepository.save(product);
    }

    @Override
    public Page<Product> findAll(String category, List<String> colors, List<String> sizes,
                                 Integer minPrice, Integer maxPrice, Integer minDiscount,
                                 String sort, String stock, Integer pageNumber, Integer pageSize) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        List<Product> products = this.productRepository.filterProducts(
                category ,minPrice, maxPrice, minDiscount, sort);

        if(!colors.isEmpty()){
            products = products.stream().filter(product -> colors.stream().anyMatch(color ->
                    color.equalsIgnoreCase(product.getColor()))).collect(Collectors.toList());
        }

        if(stock != null){
            if(stock.equals("in_stock")){
                products = products.stream().filter(p -> p.getQuantity() > 0).collect(Collectors.toList());
            }
            else if(stock.equals("out_of_stock")){
                products = products.stream().filter(p -> p.getQuantity() < 1).collect(Collectors.toList());
            }
        }

        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), products.size());

        List<Product> pageContent = products.subList(startIndex, endIndex);

        Page<Product> filteredProduct = new PageImpl<>(pageContent, pageable, products.size());
        return filteredProduct;
    }

    @Override
    public List<Product> findNewArrivals() {
        LocalDateTime currentTime = LocalDateTime.now();
        int aboutNew = 70 * 60 * 60 * 1000;
        return this.productRepository.findNewArrivals(currentTime, aboutNew);
    }

    @Override
    public Product findById(Long id) throws ProductException {
        Optional<Product> product = this.productRepository.findById(id);
        if(product == null || product.isEmpty()){
            throw new ProductException("Product not found by id# " + id);
        }
        return product.get();
    }

    @Override
    public Product findByCategoryId(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    @Override
    public Product update(Long id, Product req) throws ProductException {
        Product product = this.findById(id);

        if(req.getQuantity() != 0){
            product.setQuantity(req.getQuantity());
        }

        return this.productRepository.save(product);
    }

    @Override
    public String delete(Long id) throws ProductException {
        Product product = this.findById(id);
        product.getSizes().clear();
        this.productRepository.delete(product);
        return "Product deleted successfully";
    }
}
