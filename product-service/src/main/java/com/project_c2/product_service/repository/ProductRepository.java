package com.project_c2.product_service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.project_c2.product_service.model.Product;
public interface ProductRepository extends MongoRepository<Product, String> {
}
