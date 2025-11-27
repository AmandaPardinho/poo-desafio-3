package com.estoque.service;

import java.util.List;
import java.util.Optional;

import com.estoque.entity.Product;

public interface ProductService {
    Optional<Product> findById(int code);

    List<Product> findAll();
    
    List<Product> findByName(String name);

    List<Product> findLowStock();

    Product save(Product product);

    void delete(Product product);

    boolean deleteById(int code);

    boolean backupText(String filePath);

    boolean backupBinary(String filePath);

    boolean backupCSV(String filePath);

    String generateFileName(String extension);
}
