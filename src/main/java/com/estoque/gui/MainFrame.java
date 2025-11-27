package com.estoque.gui;

import com.estoque.controller.ProductController;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private JTabbedPane tabbedPane;
    private ProductController productController;

    public MainFrame() {
        this.productController = new ProductController();
        initializeComponents();
    }

    private void initializeComponents() {
        setTitle("Sistema de Gerenciamento de Estoque");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        createMenuBar();

        tabbedPane = new JTabbedPane();

        tabbedPane.addTab("📝 Cadastrar Produto", new ProductRegistrationPanel());
        tabbedPane.addTab("✏️ Atualizar Produto", new ProductUpdatePanel());
        tabbedPane.addTab("🗑️ Excluir Produto", new ProductDeletePanel());
        tabbedPane.addTab("📋 Listar Todos", new ListProductsPanel());
        tabbedPane.addTab("⚠️ Baixo Estoque", new ListLowStockPanel());

        add(tabbedPane, BorderLayout.CENTER);

        JPanel footer = new JPanel();
        footer.setBorder(BorderFactory.createEtchedBorder());
        JLabel lblInfo = new JLabel("Sistema de Gerenciamento de Estoque v1.0");
        footer.add(lblInfo);
        add(footer, BorderLayout.SOUTH);
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menuFile = new JMenu("Arquivo");

        JMenuItem backupTextItem = new JMenuItem("💾 Backup (Texto)");
        backupTextItem.addActionListener(e -> createBackup("txt"));

        JMenuItem backupBinaryItem = new JMenuItem("💾 Backup (Binário)");
        backupBinaryItem.addActionListener(e -> createBackup("dat"));

        JMenuItem backupCSVItem = new JMenuItem("💾 Backup (CSV)");
        backupCSVItem.addActionListener(e -> createBackup("csv"));

        JMenuItem exitItem = new JMenuItem("❌ Sair");
        exitItem.addActionListener(e -> System.exit(0));

        menuFile.add(backupTextItem);
        menuFile.add(backupBinaryItem);
        menuFile.add(backupCSVItem);
        menuFile.addSeparator();
        menuFile.add(exitItem);

        JMenu menuHelp = new JMenu("Ajuda");
        JMenuItem aboutItem = new JMenuItem("ℹ️ Sobre");
        aboutItem.addActionListener(e -> showAbout());
        menuHelp.add(aboutItem);

        menuBar.add(menuFile);
        menuBar.add(menuHelp);

        setJMenuBar(menuBar);
    }

    private void createBackup(String type) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new java.io.File(productController.generateBackupFileName(type)));

        int result = fileChooser.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            boolean success = false;

            switch (type) {
                case "txt":
                    success = productController.performTextBackup(filePath);
                    break;
                case "dat":
                    success = productController.performBinaryBackup(filePath);
                    break;
                case "csv":
                    success = productController.performCSVBackup(filePath);
                    break;
            }

            if (success) {
                JOptionPane.showMessageDialog(this,
                        "Backup realizado com sucesso!\nArquivo: " + filePath,
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Erro ao realizar backup!",
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showAbout() {
        String message = """
                Sistema de Gerenciamento de Estoque
                Versão: 1.0

                Desenvolvido como projeto acadêmico por Amanda Marques Pardinho e Ana Lívia Santos
                Disciplina: Programação Orientada a Objetos

                Funcionalidades:
                - Cadastro de produtos
                - Atualização de estoque
                - Exclusão de produtos
                - Listagem de produtos
                - Backup de dados
                """;

        JOptionPane.showMessageDialog(this, message,
                "Sobre o Sistema", JOptionPane.INFORMATION_MESSAGE);
    }

    public void updateLists() {
        Component[] components = tabbedPane.getComponents();
        for (Component comp : components) {
            if (comp instanceof ListProductsPanel) {
                ((ListProductsPanel) comp).updateList();
            } else if (comp instanceof ListLowStockPanel) {
                ((ListLowStockPanel) comp).updateList();
            }
        }
    }
}
