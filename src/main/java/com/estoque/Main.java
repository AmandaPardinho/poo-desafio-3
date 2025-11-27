package com.estoque;

import org.h2.tools.Server;
import com.estoque.repository.ConnectionBD;
import com.estoque.controller.ProductController;
import com.estoque.gui.MainFrame;

import java.awt.Font;
import java.util.Enumeration;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

public class Main {

    public static void main(String[] args) {

        try {
        // Inicia o console do H2 na porta 8082
        Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();
        System.out.println("H2 Console rodando em: http://localhost:8082");
    } catch (Exception e) {
        e.printStackTrace();
    }

        System.out.println("===========================================");
        System.out.println("Sistema de Gerenciamento de Estoque");
        System.out.println("===========================================");

        try {
            //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            configureGlobalFont(new Font("Segoe UI", Font.PLAIN, 18));
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
            UIManager.put("TabbedPane.contentBorderInsets", new java.awt.Insets(10, 10, 10, 10));
            UIManager.put("TabbedPane.tabInsets", new java.awt.Insets(5, 10, 5, 10));
            System.out.println("✓ Interface configurada");
        } catch (Exception e) {
            System.err.println("Erro ao configurar Look and Feel: " + e.getMessage());
        }

        System.out.println("\nInicializando banco de dados...");
        ConnectionBD.initializeDB();

        if (ConnectionBD.connectionTest()) {
            System.out.println("✓ Conexão com banco estabelecida!");
            populateDB();
        } else {
            System.err.println("✗ Erro ao conectar com banco de dados!");
            System.err.println("Verifique as configurações e tente novamente.");
            return;
        }

        SwingUtilities.invokeLater(() -> {
            try {
                MainFrame frame = new MainFrame();
                frame.setSize(1024, 768);
                frame.setVisible(true);
                frame.setLocationRelativeTo(null);
                System.out.println("✓ Interface gráfica iniciada!");
                System.out.println("\n===========================================");
                System.out.println("Sistema pronto para uso!");
                System.out.println("===========================================");
            } catch (Exception e) {
                System.err.println("Erro ao iniciar interface gráfica:");
                e.printStackTrace();
            }
        });
    }

    private static void populateDB() {
        System.out.println("Iniciando população de dados de teste...");
        ProductController controller = new ProductController();
        
        Object[][] produtos = {
            {"Notebook Dell Inspiron", 15},
            {"Mouse Logitech Wireless", 50},
            {"Teclado Mecânico Redragon", 30},
            {"Monitor LG 24 Polegadas", 12},
            {"Cabo HDMI 2.0 2m", 100},
            {"SSD Kingston 480GB", 25},
            {"Memória RAM DDR4 8GB", 40},
            {"Headset Gamer HyperX", 8}, // Menos de 10 (para testar filtro de estoque baixo)
            {"Webcam Full HD", 5},       // Menos de 10
            {"Roteador TP-Link Gigabit", 20},
            {"Impressora Epson EcoTank", 7}, // Menos de 10
            {"Pen Drive 64GB SanDisk", 60}
        };

        for (Object[] p : produtos) {
            try {
                // Tenta registrar. Se já existir logica de bloquear duplicado no service, vai cair no catch
                controller.registeProduct((String) p[0], (int) p[1]);
                System.out.println("Cadastrado: " + p[0]);
            } catch (Exception e) {
                System.out.println("Pulo: " + p[0] + " (" + e.getMessage() + ")");
            }
        }
        System.out.println("População concluída.\n");
    }

    private static void configureGlobalFont(Font font) {
        FontUIResource fontResource = new FontUIResource(font);
        Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, fontResource);
            }
        }
    }
}


