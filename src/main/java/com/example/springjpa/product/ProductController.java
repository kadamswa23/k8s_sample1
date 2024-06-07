package com.example.springjpa.product;

import io.micrometer.common.util.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1.0")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping("/products")
    @Operation(summary = "Get all products list by page")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "List all products by page",
                    content = { @Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", description = "Internal server error")

    })
    public ResponseEntity<List<ProductDto>> getProducts(@RequestParam(value="offset", required = false, defaultValue = "0") Integer offset,
                                                  @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                                  @RequestParam(value = "sortBy", required = false ) String sortBy){
        sortBy = StringUtils.isNotBlank(sortBy)? sortBy: "id";
        List<ProductDto> products = productService.getProducts(offset, pageSize,sortBy);
        return new ResponseEntity(products, HttpStatus.OK);
    }

    @GetMapping("/products/{code}")
    @Operation(summary = "Get product by code")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "Product by code",
                    content = { @Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", description = "Internal server error")

    })
    public ResponseEntity<List<ProductDto>> getProducts(@PathVariable String code){
        ProductDto product = productService.getProduct(code);
        return new ResponseEntity(product, HttpStatus.OK);
    }

    @PostMapping("/products")
    @Operation(summary = "Add product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "product created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<Long> saveProduct(@RequestBody ProductDto productDto){
        Product product = productService.save(productDto);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @DeleteMapping("/products/{code}")
    @Operation(summary = "Delete product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "product deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity deleteProductByCode(@PathVariable(value = "code") String code){
        productService.deleteByCode(code);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/products/{code}")
    @Operation(summary = "Update product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "product updated successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity updateProduct(@PathVariable(value = "code") String code, @RequestBody ProductDto productDto){
        productService.updateProduct(code, productDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
