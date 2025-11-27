package com.estoque.controller;

import java.util.List;
import java.util.Optional;

import com.estoque.entity.Product;
import com.estoque.service.ProductService;
import com.estoque.service.impl.ProductServiceImpl;

public class ProductController {

    private ProductService productService;

    public ProductController() {
        this.productService = new ProductServiceImpl();
    }

    public Product registeProduct(String name, int quantity) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do produto é obrigatório.");
        }

        if (quantity < 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior ou igual a zero.");
        }

        Product product = new Product(name, quantity);
        if (!product.validate()) {
            throw new IllegalArgumentException("Dados do produto inválidos.");
        }

        return productService.save(product);
    }

    public Product updateProductQuantity(int code, int newQuantity) {
        if (newQuantity < 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior ou igual a zero.");
        }

        Optional<Product> optProduct = productService.findById(code);
        if (optProduct.isEmpty()) {
            throw new IllegalArgumentException("Produto não encontrado com código: " + code);
        }

        Product product = optProduct.get();
        product.setQuantity(newQuantity);
        return productService.save(product);
    }

    public boolean deleteProduct(int code) {
        Optional<Product> optProduct = productService.findById(code);
        if (optProduct.isEmpty()) {
            throw new IllegalArgumentException("Produto não encontrado com código: " + code);
        }

        if (productService.deleteById(code)) {
            System.out.println("Produto com código " + code + " excluído com sucesso");
            return true;
        } else {
            System.out.println("Produto com código " + code + " não pode ser excluído");
            return false;
        }
    }

    public Optional<Product> findProductById(int code) {
        return productService.findById(code);
    }

    public List<Product> listAllProducts() {
        return productService.findAll();
    }

    public List<Product> listLowStockProducts() {
        return productService.findLowStock();
    }

    public List<Product> searchProductsByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return listAllProducts();
        }
        return productService.findByName(name);
    }

    public boolean performTextBackup(String filePath) {
        return productService.backupText(filePath);
    }

    public boolean performBinaryBackup(String filePath) {
        return productService.backupBinary(filePath);
    }

    public boolean performCSVBackup(String filePath) {
        return productService.backupCSV(filePath);
    }

    public String generateBackupFileName(String extension) {
        return productService.generateFileName(extension);
    }
}
