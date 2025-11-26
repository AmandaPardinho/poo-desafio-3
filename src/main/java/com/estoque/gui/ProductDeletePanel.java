package com.estoque.gui;

import com.estoque.repository.impl.ProductRepositoryImpl;
import com.estoque.entity.Product;
import javax.swing.*;
import java.awt.*;

public class ProductDeletePanel extends JPanel {

    private JTextField txtCode;
    private JTextField txtName;
    private JTextField txtQuantity;
    private JButton btnSearch;
    private JButton btnDelete;
    private JButton btnClear;
    private ProductRepositoryImpl productRepositoryImpl;
    private Product currentProduct;

    public ProductDeletePanel() {
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

        JPanel painelInfo = new JPanel(new GridBagLayout());
        painelInfo.setBorder(BorderFactory.createTitledBorder("Informações do Produto"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        painelInfo.add(new JLabel("Nome:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtName = new JTextField(30);
        txtName.setEditable(false);
        txtName.setBackground(Color.LIGHT_GRAY);
        painelInfo.add(txtName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        painelInfo.add(new JLabel("Quantidade:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txtQuantity = new JTextField(10);
        txtQuantity.setEditable(false);
        txtQuantity.setBackground(Color.LIGHT_GRAY);
        painelInfo.add(txtQuantity, gbc);

        add(painelInfo, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        btnDelete = new JButton("Excluir Produto");
        btnDelete.setPreferredSize(new Dimension(150, 35));
        btnDelete.setEnabled(false);
        btnDelete.setForeground(Color.RED);
        btnDelete.addActionListener(e -> deleteProduct());

        btnClear = new JButton("Limpar");
        btnClear.setPreferredSize(new Dimension(150, 35));
        btnClear.addActionListener(e -> clearFields());

        buttonsPanel.add(btnDelete);
        buttonsPanel.add(btnClear);

        add(buttonsPanel, BorderLayout.SOUTH);
    }

    private void searchProduct() {
        try {
            int code = Integer.parseInt(txtCode.getText().trim());

            currentProduct = productRepositoryImpl.findById(code).orElse(null);

            if (currentProduct != null) {
                txtName.setText(currentProduct.getName());
                txtQuantity.setText(String.valueOf(currentProduct.getQuantity()));
                btnDelete.setEnabled(true);
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

    private void deleteProduct() {
        if (currentProduct == null) {
            return;
        }

        int option = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja excluir o produto:\n" +
                        "Código: " + currentProduct.getCode() + "\n" +
                        "Nome: " + currentProduct.getName() + "?\n\n" +
                        "Esta ação não pode ser desfeita!",
                "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (option == JOptionPane.YES_OPTION) {
            if (productRepositoryImpl.deleteById(currentProduct.getCode())) {
                JOptionPane.showMessageDialog(this,
                        "Produto excluído com sucesso!",
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Erro ao excluir produto!",
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearFields() {
        txtCode.setText("");
        txtName.setText("");
        txtQuantity.setText("");
        btnDelete.setEnabled(false);
        currentProduct = null;
        txtCode.requestFocus();
    }
}
