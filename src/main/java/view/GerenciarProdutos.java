package view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import controller.ProdutoController;
import model.Entity.Produtos;
import model.Entity.Usuario;
import model.Repository.ProdutoRepository;
import model.Service.ProdutoService;

public class GerenciarProdutos extends JFrame {
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("BancoPie");
    private static EntityManager em = emf.createEntityManager();

    private static ProdutoRepository produtoRepository = new ProdutoRepository(em);
    private static ProdutoService produtoService = new ProdutoService(produtoRepository);
    private static ProdutoController produtoController = new ProdutoController(produtoService);

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private DefaultTableModel tableModel;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    GerenciarProdutos frame = new GerenciarProdutos(new Usuario());
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public GerenciarProdutos(Usuario usuario) {
        setTitle("Gerenciar Produtos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 735, 489);

        contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 255, 255));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JButton btnCreate = new JButton("Cadastrar Produto");
        btnCreate.setBackground(new Color(197, 197, 226));
        btnCreate.setBounds(467, 413, 178, 30);
        contentPane.add(btnCreate);
        btnCreate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showCadastroProdutoDialog();
            }
        });

        JButton btnList = new JButton("Atualizar lista");
        btnList.setBackground(new Color(197, 197, 226));
        btnList.setBounds(469, 11, 117, 25);
        contentPane.add(btnList);
        btnList.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                listAllProdutos();
            }
        });

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(20, 47, 666, 270);
        contentPane.add(scrollPane);

        table = new JTable();
        scrollPane.setViewportView(table);

        tableModel = new DefaultTableModel(
            new Object[][] {},
            new String[] {"ID", "Nome", "Preço"}
        );

        table.setModel(tableModel);

        JButton btnDelete = new JButton("Excluir");
        btnDelete.setBackground(new Color(197, 197, 226));
        btnDelete.setBounds(596, 11, 90, 25);
        contentPane.add(btnDelete);
        
        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		voltarMenuAdm(usuario);
        	}
        });
        btnVoltar.setBackground(new Color(197, 197, 226));
        btnVoltar.setBounds(33, 413, 150, 30);
        contentPane.add(btnVoltar);
        
        JButton btnEditar = new JButton("Editar");
        btnEditar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(GerenciarProdutos.this, "Por favor, selecione um produto para editar.");
                    return;
                }
                Long produtoId = (Long) table.getValueAt(selectedRow, 0);
                EditProduto(produtoId);
            }
        });
        btnEditar.setBackground(new Color(197, 197, 226));
        btnEditar.setBounds(369, 11, 90, 25);
        contentPane.add(btnEditar);
        
        JLabel lblNewLabel = new JLabel("Gestão De Produtos");
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblNewLabel.setBounds(88, 15, 205, 13);
        contentPane.add(lblNewLabel);
        
        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteProduto();
            }
        });

        // Automatically list all products when the frame opens
        listAllProdutos();
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }
    
    private void showCadastroProdutoDialog() {
        CadastroProduto dialog = new CadastroProduto();
        dialog.setVisible(true);
    }

    private void listAllProdutos() {
        tableModel.setRowCount(0);
        List<Produtos> produtos = produtoRepository.findAll();
        for (Produtos produto : produtos) {
            Object[] rowData = {produto.getId(), produto.getNome(), produto.getPreco()};
            tableModel.addRow(rowData);
        }
    }

    private void EditProduto(Long produtoId) {
        // Encontre o produto pelo ID
        Produtos produto = produtoController.findProdutoByid(produtoId);
        
        if (produto != null) {
            // Crie uma instância de EditProductFrame com o produto encontrado e a referência ao frame pai (GerenciarProdutos)
            EditProductFrame dialog = new EditProductFrame(produto);    
            dialog.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Produto não encontrado para edição.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void voltarMenuAdm(Usuario usuario) {
        dispose();
        AdminMenuFrame menuadm = new AdminMenuFrame(usuario);
        menuadm.setVisible(true);
    }

    private void deleteProduto() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione um produto para excluir.");
            return;
        }

        long produtoId = (long) table.getValueAt(selectedRow, 0);
        int option = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir este produto?",
                                                    "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
        if (option != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            produtoController.deleteProdrById(produtoId);
            JOptionPane.showMessageDialog(this, "Produto excluído com sucesso.");
            listAllProdutos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao excluir produto: " + ex.getMessage());
        }
    }
}
