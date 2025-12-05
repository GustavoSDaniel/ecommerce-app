package com.gustavosdaniel.ecommerce_api.product;

import com.gustavosdaniel.ecommerce_api.category.Category;
import com.gustavosdaniel.ecommerce_api.orderItem.OrderItem;
import com.gustavosdaniel.ecommerce_api.util.AuditableBase;
import jakarta.persistence.*;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Product extends AuditableBase {

    public Product() {}

    public Product(String name, String description, MeasureUnit measureUnit, BigDecimal availableQuantity, BigDecimal price) {
        this.name = name;
        this.description = description;
        this.measureUnit = measureUnit;
        this.availableQuantity = availableQuantity;
        this.price = price;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "measure_unit" ,nullable = false)
    private MeasureUnit measureUnit;

    @Column(nullable = false, precision = 19, scale = 3)
    private BigDecimal availableQuantity;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany( mappedBy = "product",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();

    public void handleStockOperation(BigDecimal quantity, StockOperationType type)
            throws StockOperationExceptionAddAndRemove, StockOperationExceptionSet, insuficienteStockException {

        if (type != StockOperationType.SET &&
                quantity.compareTo(BigDecimal.ZERO) <= 0){

            throw new StockOperationExceptionAddAndRemove();
        }

        if (type == StockOperationType.SET &&
                quantity.compareTo(BigDecimal.ZERO) < 0){

            throw new StockOperationExceptionSet();

        }

        switch (type) {

            case ADD -> {
                this.availableQuantity = this.availableQuantity.add(quantity);
            }
            case REMOVE -> {

                BigDecimal newStock = this.availableQuantity.subtract(quantity);

                if (newStock.compareTo(BigDecimal.ZERO) < 0) {

                    throw new insuficienteStockException();
                }
                this.availableQuantity = newStock;
            }
            case SET ->
                this.availableQuantity = quantity;

        }

    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MeasureUnit getMeasureUnit() {
        return measureUnit;
    }

    public void setMeasureUnit(MeasureUnit measureUnit) {
        this.measureUnit = measureUnit;
    }

    public BigDecimal getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(BigDecimal availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
