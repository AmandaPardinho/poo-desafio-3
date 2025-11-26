package com.estoque.gui;

import com.estoque.repository.impl.ProductRepositoryImpl;
import com.estoque.entity.Product;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ListLowStockPanel extends JPanel{
    
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnUpdate;
    private JLabel lblTotal;
    private ProductRepositoryImpl productRepositoryImpl;
    
    public ListLowStockPanel() {
        this.productRepositoryImpl = new ProductRepositoryImpl();
        initializeComponents();
        updateList();
    }
    
    private void initializeComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createTitledBorder("Produtos com Estoque Baixo - menos de 10 unidades"));
        
        btnUpdate = new JButton("Atualizar Lista");
        btnUpdate.addActionListener(e -> updateList());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(btnUpdate);

        JLabel lblAviso = new JLabel("⚠️ Produtos que necessitam reposição");
        lblAviso.setForeground(Color.RED);
        lblAviso.setFont(new Font("Arial", Font.BOLD, 12));
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(lblAviso);
        
        buttonPanel.add(buttonPanel, BorderLayout.NORTH);
        
        add(buttonPanel, BorderLayout.NORTH);
        
        String[] columns = {"Código", "Nome do Produto", "Quantidade", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        
        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(1).setPreferredWidth(300);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(120);
        
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblTotal = new JLabel("Produtos com baixo estoque: 0");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 12));
        bottomPanel.add(lblTotal);
        
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    public void updateList() {
        tableModel.setRowCount(0);
        
        List<Product> products = productRepositoryImpl.findLowStock();
        
        for (Product p : products) {
            String status = getStatus(p.getQuantity());
            Object[] row = {
                p.getCode(),
                p.getName(),
                p.getQuantity(),
                status
            };
            tableModel.addRow(row);
        }
        
        lblTotal.setText("Produtos com baixo estoque: " + products.size());
        
        long critical = products.stream()
            .filter(p -> p.getQuantity() == 0)
            .count();
        
        if (critical > 0) {
            lblTotal.setText(lblTotal.getText() + " | ⚠️ ATENÇÃO: " + critical + " produto(s) sem estoque!");
            lblTotal.setForeground(Color.RED);
        } else {
            lblTotal.setForeground(Color.BLACK);
        }
    }
    
    private String getStatus(int quantity) {
        if (quantity == 0) {
            return "🔴 SEM ESTOQUE";
        } else if (quantity < 5) {
            return "🟠 CRÍTICO";
        } else {
            return "🟡 BAIXO";
        }
    }
}

