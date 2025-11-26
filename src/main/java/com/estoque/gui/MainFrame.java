package com.estoque.gui;

//import com.estoque.service.BackupService;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame{
    private JTabbedPane tabbedPane;
    private BackupService backupService;
    
    public MainFrame() {
        this.backupService = new BackupService();
        inicializarComponentes();
    }
    
    private void inicializarComponentes() {
        setTitle("Sistema de Gerenciamento de Estoque");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        
        // Criar menu
        criarMenuBar();
        
        // Criar abas
        tabbedPane = new JTabbedPane();
        
        tabbedPane.addTab("Cadastrar Produto", new CadastroProdutoPanel());
        tabbedPane.addTab("Atualizar Produto", new ProductUpdatePanel());
        tabbedPane.addTab("Excluir Produto", new ExcluirProdutoPanel());
        tabbedPane.addTab("Listar Todos", new ListarProdutosPanel());
        tabbedPane.addTab("Baixo Estoque", new ListarBaixoEstoquePanel());
        
        add(tabbedPane, BorderLayout.CENTER);
        
        // Painel de rodapé
        JPanel rodape = new JPanel();
        rodape.setBorder(BorderFactory.createEtchedBorder());
        JLabel lblInfo = new JLabel("Sistema de Gerenciamento de Estoque v1.0 | Desenvolvido para POO");
        rodape.add(lblInfo);
        add(rodape, BorderLayout.SOUTH);
    }

    private void criarMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // Menu Arquivo
        JMenu menuArquivo = new JMenu("Arquivo");
        
        JMenuItem itemBackupTexto = new JMenuItem("Backup (Texto)");
        itemBackupTexto.addActionListener(e -> realizarBackup("txt"));
        
        JMenuItem itemBackupBinario = new JMenuItem("Backup (Binário)");
        itemBackupBinario.addActionListener(e -> realizarBackup("dat"));
        
        JMenuItem itemBackupCSV = new JMenuItem("Backup (CSV)");
        itemBackupCSV.addActionListener(e -> realizarBackup("csv"));
        
        JMenuItem itemSair = new JMenuItem("Sair");
        itemSair.addActionListener(e -> System.exit(0));
        
        menuArquivo.add(itemBackupTexto);
        menuArquivo.add(itemBackupBinario);
        menuArquivo.add(itemBackupCSV);
        menuArquivo.addSeparator();
        menuArquivo.add(itemSair);
        
        // Menu Ajuda
        JMenu menuAjuda = new JMenu("Ajuda");
        JMenuItem itemSobre = new JMenuItem("Sobre");
        itemSobre.addActionListener(e -> mostrarSobre());
        menuAjuda.add(itemSobre);
        
        menuBar.add(menuArquivo);
        menuBar.add(menuAjuda);
        
        setJMenuBar(menuBar);
    }
    
    private void realizarBackup(String tipo) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new java.io.File(backupService.gerarNomeArquivoBackup(tipo)));
        
        int resultado = fileChooser.showSaveDialog(this);
        
        if (resultado == JFileChooser.APPROVE_OPTION) {
            String caminho = fileChooser.getSelectedFile().getAbsolutePath();
            boolean sucesso = false;
            
            switch (tipo) {
                case "txt":
                    sucesso = backupService.backupTexto(caminho);
                    break;
                case "dat":
                    sucesso = backupService.backupBinario(caminho);
                    break;
                case "csv":
                    sucesso = backupService.backupCSV(caminho);
                    break;
            }
            
            if (sucesso) {
                JOptionPane.showMessageDialog(this, 
                    "Backup realizado com sucesso!\nArquivo: " + caminho,
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Erro ao realizar backup!",
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void mostrarSobre() {
        String mensagem = """
            Sistema de Gerenciamento de Estoque
            Versão: 1.0
            
            Desenvolvido como projeto acadêmico
            Disciplina: Programação Orientada a Objetos
            
            Funcionalidades:
            - Cadastro de produtos
            - Atualização de estoque
            - Exclusão de produtos
            - Listagem de produtos
            - Backup de dados
            """;
        
        JOptionPane.showMessageDialog(this, mensagem, 
            "Sobre o Sistema", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Atualiza todas as abas de listagem
     */
    public void atualizarListagens() {
        Component[] components = tabbedPane.getComponents();
        for (Component comp : components) {
            if (comp instanceof ListarProdutosPanel) {
                ((ListarProdutosPanel) comp).atualizarLista();
            } else if (comp instanceof ListarBaixoEstoquePanel) {
                ((ListarBaixoEstoquePanel) comp).atualizarLista();
            }
        }
    }
}
