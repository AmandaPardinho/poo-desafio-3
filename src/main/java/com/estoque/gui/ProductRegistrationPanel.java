package com.estoque.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import com.estoque.entity.Product;
import com.estoque.repository.impl.ProductRepositoryImpl;

public class ProductRegistrationPanel extends JPanel{
    private JTextField txtName;
    private JSpinner spnQuantity;
    private JButton btnRegister;
    private JButton btnClear;
    private ProductRepositoryImpl productRepositoryImpl;
    
    public ProductRegistrationPanel() {
        this.productRepositoryImpl = new ProductRepositoryImpl();
        initializeComponents();
    }
    
    private void initializeComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Dados do Produto"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Nome do Produto:"), gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtName = new JTextField(30);
        formPanel.add(txtName, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Quantidade:"), gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        SpinnerNumberModel model = new SpinnerNumberModel(0, 0, 999999, 1);
        spnQuantity = new JSpinner(model);
        JSpinner.NumberEditor editor = new JSpinner.NumberEditor(spnQuantity, "#");
        spnQuantity.setEditor(editor);
        formPanel.add(spnQuantity, gbc);
        
        add(formPanel, BorderLayout.NORTH);
        
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        btnRegister = new JButton("Cadastrar Produto");
        btnRegister.setPreferredSize(new Dimension(150, 35));
        btnRegister.addActionListener(e -> registerProduct());
        
        btnClear = new JButton("Limpar");
        btnClear.setPreferredSize(new Dimension(150, 35));
        btnClear.addActionListener(e -> clearFields());
        
        buttonsPanel.add(btnRegister);
        buttonsPanel.add(btnClear);
        
        add(buttonsPanel, BorderLayout.CENTER);
        
        JPanel instructionsPanel = new JPanel();
        instructionsPanel.setBorder(BorderFactory.createTitledBorder("Instruções"));
        JTextArea txtInstrutions = new JTextArea(
            "1. Digite o nome do produto (obrigatório)\n" +
            "2. Informe a quantidade em estoque (deve ser >= 0)\n" +
            "3. Clique em 'Cadastrar Produto' para salvar\n\n" +
            "O código do produto será gerado automaticamente pelo sistema."
        );
        txtInstrutions.setEditable(false);
        txtInstrutions.setBackground(instructionsPanel.getBackground());
        txtInstrutions.setFont(new Font("Arial", Font.PLAIN, 36));
        instructionsPanel.add(txtInstrutions);
        
        add(instructionsPanel, BorderLayout.SOUTH);
    }
    
    private void registerProduct() {
        String name = txtName.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "O nome do produto é obrigatório!",
                "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            txtName.requestFocus();
            return;
        }
    
        int quantity = (Integer) spnQuantity.getValue();
        
        Product product = new Product(name, quantity);
        
        if (!product.validate()) {
            JOptionPane.showMessageDialog(this,
                "Dados inválidos! Verifique se a quantidade é maior ou igual a zero.",
                "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Product insertedProduct = productRepositoryImpl.insert(product);
        if (insertedProduct != null) {
            JOptionPane.showMessageDialog(this,
                "Produto cadastrado com sucesso!\nCódigo: " + insertedProduct.getCode(),
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this,
                "Erro ao cadastrar produto!",
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void clearFields() {
        txtName.setText("");
        spnQuantity.setValue(0);
        txtName.requestFocus();
    }
}
