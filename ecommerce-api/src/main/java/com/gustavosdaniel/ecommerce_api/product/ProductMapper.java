package com.gustavosdaniel.ecommerce_api.product;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Objects;

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

    void updateProduct(ProductUpdateRequest request, Product product){

        if (request == null){

            return;
        }

        if(request.name() != null && !request.name().isBlank()){

            product.setName(request.name());
        }

        if(request.description() != null && !request.description().isBlank()){

            product.setDescription(request.description());
        }

        if (request.measureUnit() != null && !Objects.equals(request.measureUnit(), product.getMeasureUnit())) {

            product.setMeasureUnit(request.measureUnit());
        }

        if (request.availableQuantity() != null &&
                !Objects.equals(request.availableQuantity(), product.getAvailableQuantity())) {

            product.setAvailableQuantity(request.availableQuantity());
        }

        if (request.price() != null && !Objects.equals(request.price(), product.getPrice())) {

            product.setPrice(request.price());
        }
    }

    public ProductUpdateResponse toProductUpdateResponse(Product product){

        if(product == null){
            return null;
        }

        return new ProductUpdateResponse(

                product.getCategory() != null ? product.getCategory().getName() : null,
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getMeasureUnit(),
                product.getAvailableQuantity(),
                product.getPrice(),
                product.getLastModifiedBy()
        );
    }

    public StockUpdateResponse toStockUpdateResponse(
            Product product, StockOperationType operationType, BigDecimal quantity){

        if(product == null){
            return null;
        }

        return new StockUpdateResponse(
                product.getId(),
                product.getName(),
                operationType,
                quantity,
                product.getAvailableQuantity()
        );
    }
}
