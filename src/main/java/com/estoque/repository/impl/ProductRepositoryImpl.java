package com.estoque.repository.impl;

import com.estoque.entity.Product;
import com.estoque.repository.ConnectionBD;
import com.estoque.repository.ProductRepository;

import java.io.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductRepositoryImpl implements ProductRepository {
    
    @Override
    public Optional<Product> findById(int code) {
        String sql = "SELECT * FROM produtos WHERE codigo = ?";
        
        try (Connection conn = ConnectionBD.getConnect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, code);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Product product = new Product(
                        rs.getInt("codigo"),
                        rs.getString("nome"),
                        rs.getInt("quantidade")
                    );
                    return Optional.of(product);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao buscar produto: " + e.getMessage());
            e.printStackTrace();
        }
        
        return Optional.empty();
    }
    
    @Override
    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM produtos ORDER BY codigo";
        
        try (Connection conn = ConnectionBD.getConnect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Product product = new Product(
                    rs.getInt("codigo"),
                    rs.getString("nome"),
                    rs.getInt("quantidade")
                );
                products.add(product);
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao listar produtos: " + e.getMessage());
            e.printStackTrace();
        }
        
        return products;
    }
    
    @Override
    public List<Product> findByName(String name) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM produtos WHERE nome LIKE ? ORDER BY nome";
        
        try (Connection conn = ConnectionBD.getConnect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + name + "%");
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product(
                        rs.getInt("codigo"),
                        rs.getString("nome"),
                        rs.getInt("quantidade")
                    );
                    products.add(product);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao buscar produtos por nome: " + e.getMessage());
            e.printStackTrace();
        }
        
        return products;
    }
    
    @Override
    public Product save(Product product) {
        if (product.getCode() == 0) {
            return insert(product);
        } else {
            return update(product);
        }
    }
    
    @Override
    public Product insert(Product product) {
        String sql = "INSERT INTO produtos (nome, quantidade) VALUES (?, ?)";
        
        try (Connection conn = ConnectionBD.getConnect();
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, product.getName());
            pstmt.setInt(2, product.getQuantity());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        product.setCode(rs.getInt(1));
                    }
                }
                return product;
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao inserir produto: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    @Override
    public Product update(Product product) {
        String sql = "UPDATE produtos SET nome = ?, quantidade = ? WHERE codigo = ?";
        
        try (Connection conn = ConnectionBD.getConnect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, product.getName());
            pstmt.setInt(2, product.getQuantity());
            pstmt.setInt(3, product.getCode());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                return product;
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar produto: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    @Override
    public void delete(Product product) {
        deleteById(product.getCode());
    }
    
    @Override
    public boolean deleteById(int code) {
        String sql = "DELETE FROM produtos WHERE codigo = ?";
    
        try (Connection conn = ConnectionBD.getConnect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, code);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Erro ao excluir produto: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean backupText(String filePath) {
        List<Product> products = findAll();
        
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
        List<Product> products = findAll();
        
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
        List<Product> products = findAll();
        
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

