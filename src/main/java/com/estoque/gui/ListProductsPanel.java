package com.estoque.gui;

import com.estoque.repository.impl.ProductRepositoryImpl;
import com.estoque.entity.Product;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ListProductsPanel extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnUpdate;
    private JLabel lblTotal;
    private ProductRepositoryImpl productRepositoryImpl;

    public ListProductsPanel() {
        this.productRepositoryImpl = new ProductRepositoryImpl();
        initializeComponents();
        updateList();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createTitledBorder("Todos os Produtos"));

        btnUpdate = new JButton("Atualizar Lista");
        btnUpdate.addActionListener(e -> updateList());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(btnUpdate);

        topPanel.add(buttonPanel, BorderLayout.NORTH);

        add(topPanel, BorderLayout.NORTH);

        String[] columns = { "Código", "Nome do Produto", "Quantidade" };
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
        table.getColumnModel().getColumn(1).setPreferredWidth(400);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblTotal = new JLabel("Total de produtos: 0");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 12));
        bottomPanel.add(lblTotal);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void updateList() {

        tableModel.setRowCount(0);

        List<Product> products = productRepositoryImpl.findAll();

        for (Product p : products) {
            Object[] row = {
                    p.getCode(),
                    p.getName(),
                    p.getQuantity()
            };
            tableModel.addRow(row);
        }

        lblTotal.setText("Total de produtos: " + products.size());
    }
}
