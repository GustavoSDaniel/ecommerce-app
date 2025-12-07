package com.gustavosdaniel.ecommerce_api.product;

import com.gustavosdaniel.ecommerce_api.category.Category;
import com.gustavosdaniel.ecommerce_api.category.CategoryNotFoundException;
import com.gustavosdaniel.ecommerce_api.category.CategoryRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private static final  Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    public ProductServiceImpl(ProductMapper productMapper, ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productMapper = productMapper;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public ProductResponse createProduct(Integer categoryId, ProductRequest productRequest) {

        log.info("Criando produto {}", productRequest.name());

        Category category = categoryRepository
                .findById(categoryId).orElseThrow(CategoryNotFoundException::new);

        if (productRepository.existsByNameIgnoreCase(productRequest.name())){

            log.info("Tentativa de criar produto duplicado: {}", productRequest.name());

            throw new ProductNameExistsException();

        }

        Product newProduct = productMapper.toProduct(productRequest);
        newProduct.setCategory(category);

        Product savedProduct = productRepository.save(newProduct);

        log.info("Produto Criado com sucesso {}", savedProduct.getName());

        return productMapper.toProductResponse(savedProduct);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public Page<ProductResponse> getAllProducts(Pageable pageable) {

        log.info("Buscando todos os produtos {}", pageable.getSort());

        Page<Product> products = productRepository.findAll(pageable);

        log.info("Produtos buscados com sucesso {}", products.getTotalElements());

        return products.map(productMapper::toProductResponse);

    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public Page<ProductResponse> getAllActiveProducts(Pageable pageable) {

        log.info("Buscando todos os produtos ativos ");

        Page<Product> products = productRepository.findActiveProducts(pageable);

        log.info("Produtos buscados com sucesso {}", products.getTotalElements());

        return products.map(productMapper::toProductResponse);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public Page<ProductResponse> getAllActiveFalseProducts(Pageable pageable) {

        log.info("Buscando todos os produtos inativos ");

        Page<Product> products = productRepository.findByActiveFalse(pageable);

        if (products.isEmpty()){

            log.info("Nenhum produto inativo encontrado");
        }

        log.info("Produtos inativos buscados com sucesso {}", products.getTotalElements());

        return products.map(productMapper::toProductResponse);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public Page<ProductResponse> getAllProductsByCategoryId(Integer categoryId, Pageable pageable) {

        log.info("Buscando produtos da categoria ID: {}", categoryId);

        Page<Product> products = productRepository.findByCategory_Id(categoryId, pageable);

        if (products.isEmpty()){

            log.info("Nenhum produto encontrado na categoria ID: {}", categoryId);

            return Page.empty();
        }

        log.info("Produto encontrado com sucesso: {}", products.getTotalElements());

        return products.map(productMapper::toProductResponse);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public Page<ProductResponse> searchProductByName(String name, Pageable pageable) {

        log.info("Buscando produtos pelo nome {}", name);

        if (name == null || name.isBlank()){

            log.warn("Tentativa de busca com nome vazio");

            return Page.empty();
        }

        Page<Product> products = productRepository.findByNameContainingIgnoreCase(name, pageable);

        if (products.isEmpty()){

            log.info("Nenhum produto com esse nome foi encontrado: {}", name);
        }

        log.info("Produto encontrado com sucesso {}", products.getTotalElements());

        return products.map(productMapper::toProductResponse);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public ProductResponse getProductById(Long productId) {

        log.info("Buscando produto através do ID {}", productId);

        Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);

        log.info("Produto encontrado com sucesso: {}", product.getName());

        return productMapper.toProductResponse(product);

    }

    @Override
    @Transactional
    public ProductUpdateResponse updateProduct(Long productId, ProductUpdateRequest productRequest) {

        log.info("Iniciando atualização do produto ID: {}", productId);

        Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);

        if (productRequest.name() != null &&
                !productRequest.name().equals(product.getName())
                && productRepository.existsByNameIgnoreCase(productRequest.name())) {

            log.warn("Tentativa de atualizar para nome já existente: {}", productRequest.name());
            throw new ProductNameExistsException();

        }

        productMapper.updateProduct(productRequest, product);

        Product updatedProduct = productRepository.save(product);

        log.info("Produto atualizado com sucesso: {}", updatedProduct.getId());

        return productMapper.toProductUpdateResponse(updatedProduct);
    }

    @Override
    @Transactional
    public StockUpdateResponse updateStock(Long productId, StockUpdateRequest stockUpdateDTO)
            throws StockOperationExceptionAddAndRemove, StockOperationExceptionSet, InsuficienteStockException {

        log.info("Atualizando estoque do produto ID: {}", productId);

        Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);

        product.handleStockOperation(stockUpdateDTO.quantity(), stockUpdateDTO.stockOperationType());

        Product updateProduct = productRepository.save(product);

        log.info("Estoque atualizado. Prod: {} | Operação: {} | Qtd: {} | Novo Saldo: {}",
                productId, stockUpdateDTO.stockOperationType(),
                stockUpdateDTO.quantity(),
                product.getAvailableQuantity());

        return productMapper.toStockUpdateResponse(updateProduct,
                stockUpdateDTO.stockOperationType(),
                stockUpdateDTO.quantity());
    }

    @Override
    @Transactional
    public void reactivateProduct(Long productId) {

        log.info("Reativando produto {}", productId);

        Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);

        if (Boolean.TRUE.equals(product.getActive())){

            log.info("Produto {} já esta ativado ", product.getName());

            return;
        }

        product.setActive(true);

        productRepository.save(product);

        log.info("Produto {} Ativado com sucesso ", product.getName());

    }

    @Override
    @Transactional
    public void desativeProduct(Long productId) {

        log.info("Desativando produto {}", productId);

        Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);

        if (Boolean.FALSE.equals(product.getActive())){

            log.info("Produto {} já esta desativado ", product.getName());

            return;
        }

        product.setActive(false);

        productRepository.save(product);

        log.info("Produto {} Desativado com sucesso ", product.getName());

    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {

        log.info("Deletando produto {}", id);

        Product product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);

        if (!product.getOrderItems().isEmpty()) {

            log.info("Náo foi possivel excluir produto {}", product.getName());

            throw new ProductAssociateWithOrdersException();
        }

        log.info("Produto {} Deletado com sucesso ", product.getName());

        productRepository.delete(product);

    }
}
