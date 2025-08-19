package dev.mvasylenko.service;

import dev.mvasylenko.entity.ProductModel;

public interface ProductService {
    String createProduct(ProductModel product) throws Exception;
}
