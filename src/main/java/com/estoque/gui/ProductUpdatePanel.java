package com.estoque.gui;

import com.estoque.repository.impl.ProductRepositoryImpl;
import com.estoque.entity.Product;
import javax.swing.*;
import java.awt.*;


public class ProductUpdatePanel extends JPanel {
    
    private JTextField txtCode;
    private JTextField txtCurrentName;
    private JSpinner spnNewQuantity;
    private JButton btnSearch;
    private JButton btnUpdate;
    private JButton btnClear;
    private ProductRepositoryImpl productRepositoryImpl;
    private Product currentProduct;
    
    public ProductUpdatePanel() {
        this.productRepositoryImpl = new ProductRepositoryImpl();
        initializeComponents();
    }
    
    private void initializeComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Buscar Produto"));
        
        searchPanel.add(new JLabel("Código do Produto:"));
        txtCode = new JTextField(10);
        searchPanel.add(txtCode);
        
        btnSearch = new JButton("Buscar");
        btnSearch.addActionListener(e -> searchProduct());
        searchPanel.add(btnSearch);
        
        add(searchPanel, BorderLayout.NORTH);
        
        JPanel updatePanel = new JPanel(new GridBagLayout());
        updatePanel.setBorder(BorderFactory.createTitledBorder("Atualizar Quantidade"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        updatePanel.add(new JLabel("Produto:"), gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtCurrentName = new JTextField(30);
        txtCurrentName.setEditable(false);
        txtCurrentName.setBackground(Color.LIGHT_GRAY);
        updatePanel.add(txtCurrentName, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        updatePanel.add(new JLabel("Nova Quantidade:"), gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        SpinnerNumberModel model = new SpinnerNumberModel(0, 0, 999999, 1);
        spnNewQuantity = new JSpinner(model);
        spnNewQuantity.setEnabled(false);
        JSpinner.NumberEditor editor = new JSpinner.NumberEditor(spnNewQuantity, "#");
        spnNewQuantity.setEditor(editor);
        updatePanel.add(spnNewQuantity, gbc);
        
        add(updatePanel, BorderLayout.CENTER);
        
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        btnUpdate = new JButton("Atualizar Produto");
        btnUpdate.setPreferredSize(new Dimension(150, 35));
        btnUpdate.setEnabled(false);
        btnUpdate.addActionListener(e -> updateProduct());
        
        btnClear = new JButton("Limpar");
        btnClear.setPreferredSize(new Dimension(150, 35));
        btnClear.addActionListener(e -> clearFields());
        
        buttonsPanel.add(btnUpdate);
        buttonsPanel.add(btnClear);
        
        add(buttonsPanel, BorderLayout.SOUTH);
    }
    
    private void searchProduct() {
        try {
            int code = Integer.parseInt(txtCode.getText().trim());
            
            currentProduct = productRepositoryImpl.findById(code).orElse(null);
            
            if (currentProduct != null) {
                txtCurrentName.setText(currentProduct.getName() + " (Qtd. atual: " + currentProduct.getQuantity() + ")");
                spnNewQuantity.setValue(currentProduct.getQuantity());
                spnNewQuantity.setEnabled(true);
                btnUpdate.setEnabled(true);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Produto não encontrado!",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
                clearFields();
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Código inválido! Digite apenas números.",
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateProduct() {
        if (currentProduct == null) {
            return;
        }
        
        int newQuantity = (Integer) spnNewQuantity.getValue();
        
        if (newQuantity < 0) {
            JOptionPane.showMessageDialog(this,
                "A quantidade deve ser maior ou igual a zero!",
                "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        currentProduct.setQuantity(newQuantity);
        Product updatedProduct = productRepositoryImpl.update(currentProduct);
        if (updatedProduct != null) {
            JOptionPane.showMessageDialog(this,
                "Quantidade atualizada com sucesso!",
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this,
                "Erro ao atualizar produto!",
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void clearFields() {
        txtCode.setText("");
        txtCurrentName.setText("");
        spnNewQuantity.setValue(0);
        spnNewQuantity.setEnabled(false);
        btnUpdate.setEnabled(false);
        currentProduct = null;
        txtCode.requestFocus();
    }
}
