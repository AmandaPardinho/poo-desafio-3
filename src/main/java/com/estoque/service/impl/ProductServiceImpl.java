package com.estoque.service.impl;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import com.estoque.entity.Product;
import com.estoque.repository.ProductRepository;
import com.estoque.service.ProductService;

public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    @Override
    public Optional<Product> findById(int code) {
        return productRepository.findById(code);
    }

    @Override
    public List<Product> findByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public Product save(Product product) {
        if (product.getCode() == 0) {
            List<Product> existingProducts = productRepository.findByName(product.getName());
            if (!existingProducts.isEmpty()) {
                throw new RuntimeException("Já existe um produto com este nome.");
            }
        }
        return productRepository.save(product);
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public void delete(Product product) {
        productRepository.delete(product);
    }

    @Override
    public void deleteById(int code) {
        productRepository.deleteById(code);
    }

    @Override
    public boolean backupText(String filePath) {
        List<Product> products = productRepository.findAll();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {

            writer.write("=================================================");
            writer.newLine();
            writer.write("BACKUP DE PRODUTOS - SISTEMA DE ESTOQUE");
            writer.newLine();
            writer.write("Data/Hora: " + LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            writer.newLine();
            writer.write("Total de produtos: " + products.size());
            writer.newLine();
            writer.write("=================================================");
            writer.newLine();
            writer.newLine();

            for (Product p : products) {
                writer.write(String.format("Código: %d | Nome: %s | Quantidade: %d",
                        p.getCode(), p.getName(), p.getQuantity()));
                writer.newLine();
            }

            System.out.println("Backup em texto realizado com sucesso!");
            return true;

        } catch (IOException e) {
            System.err.println("Erro ao realizar backup em texto: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean backupBinary(String filePath) {
        List<Product> products = productRepository.findAll();

        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(filePath))) {

            oos.writeObject(products);
            System.out.println("Backup binário realizado com sucesso!");
            return true;

        } catch (IOException e) {
            System.err.println("Erro ao realizar backup binário: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean backupCSV(String filePath) {
        List<Product> products = productRepository.findAll();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {

            writer.write("Codigo,Nome,Quantidade");
            writer.newLine();

            for (Product p : products) {
                writer.write(String.format("%d,\"%s\",%d",
                        p.getCode(), p.getName(), p.getQuantity()));
                writer.newLine();
            }

            System.out.println("Backup CSV realizado com sucesso!");
            return true;

        } catch (IOException e) {
            System.err.println("Erro ao realizar backup CSV: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String generateFileName(String extension) {
        String dateTime = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        return "backup_produtos_" + dateTime + "." + extension;
    }
}
