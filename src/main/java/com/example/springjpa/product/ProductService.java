package com.example.springjpa.product;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private static Logger log = LoggerFactory.getLogger(ProductService.class);
    @Autowired
    ProductRepository productRepository;

    public List<ProductDto> getProducts(Integer offset, Integer pageSize, String sortBy) {
        PageRequest pagable = PageRequest.of(offset, pageSize, Sort.by(sortBy));
        Page<Product> productPage = productRepository.findAll(pagable);

        List<Product> usersList = productPage.getContent();
        return  usersList.stream()
                .map( product -> {
                    ProductDto productDto = ProductDto.builder()
                            .code(product.getCode())
                            .description(product.getDescription())
                            .name(product.getName())
                            .category(product.getCategory())
                            .price(product.getPrice())
                            .build();

                    return productDto;
                })
                .collect(Collectors.toList());

    }

    public Product save(ProductDto productDto) {
        try{
            Product product = Product.builder()
                    .code(productDto.getCode())
                    .name(productDto.getName())
                    .description(productDto.getDescription())
                    .category(productDto.getCategory())
                    .price(productDto.getPrice())
                    .build();

            return productRepository.save(product);
        }catch (Exception ex){
            log.error(ex.getMessage());
            throw ex;
        }
    }
    public void deleteByCode(String code) {
        Product product =  productRepository.findByCode(code).orElseThrow( () -> new EntityNotFoundException("Product not found "));
        try{
            productRepository.deleteById(product.getId());
        }catch (Exception ex){
            log.error("User can not be deleted "+ex.getMessage());
            throw ex;
        }
    }

    public void updateProduct(String code, ProductDto productDto) {
        Product product =  productRepository.findByCode(code).orElseThrow( () -> new EntityNotFoundException("Product not found "));
        try{
                product.setName(productDto.getName());
                product.setDescription(productDto.getDescription());
                product.setCategory(productDto.getCategory());
                product.setPrice(productDto.getPrice());
                productRepository.save(product);
        }catch (Exception ex){
            log.error("Product can not be deleted "+ex.getMessage());
            throw ex;
        }
    }

    public ProductDto getProduct(String code) {
        Product product =  productRepository.findByCode(code).orElseThrow( () -> new EntityNotFoundException("Product not found "));
        return ProductDto.builder()
                .code(product.getCode())
                .description(product.getDescription())
                .name(product.getName())
                .category(product.getCategory())
                .price(product.getPrice())
                .build();
    }
}
