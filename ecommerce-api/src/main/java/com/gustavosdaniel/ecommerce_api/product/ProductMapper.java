package com.gustavosdaniel.ecommerce_api.product;

import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toProduct(ProductRequest request){

        if(request == null){
            return null;
        }

        return new Product(
                request.name(),
                request.description(),
                request.measureUnit(),
                request.availableQuantity(),
                request.price()

        );
    }

    public ProductResponse toProductResponse(Product product){
        if(product == null){
            return null;
        }

        return new ProductResponse(

                product.getCategory() != null ? product.getCategory().getName() : null,
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getMeasureUnit(),
                product.getAvailableQuantity(),
                product.getPrice(),
                product.getCreatedBy()

        );
    }
}
