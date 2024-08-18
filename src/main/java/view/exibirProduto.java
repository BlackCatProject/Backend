package view;

import java.awt.Color;
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

import model.Entity.Produtos;
import model.Entity.Usuario;
import model.Repository.ProdutoRepository;

public class exibirProduto extends JFrame {
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("BancoPie");
    private static EntityManager em = emf.createEntityManager();

    private static ProdutoRepository produtoRepository = new ProdutoRepository(em);

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private DefaultTableModel tableModel;

    public exibirProduto(Usuario usuario) {
        setTitle("Gerenciar Produtos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 735, 489);

        contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 255, 255));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

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

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		voltarMenuFuncionario(usuario);
        	}
        });
        btnVoltar.setBackground(new Color(197, 197, 226));
        btnVoltar.setBounds(33, 413, 150, 30);
        contentPane.add(btnVoltar);

        JLabel lblNewLabel = new JLabel("Gestão De Produtos");
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblNewLabel.setBounds(88, 15, 205, 13);
        contentPane.add(lblNewLabel);

        // Automatically list all products when the frame opens
        listAllProdutos();
    }

    private void listAllProdutos() {
        tableModel.setRowCount(0);
        List<Produtos> produtos = produtoRepository.findAll();
        for (Produtos produto : produtos) {
            Object[] rowData = {produto.getId(), produto.getNome(), produto.getPreco()};
            tableModel.addRow(rowData);
        }
    }

    private void voltarMenuFuncionario(Usuario usuario) {
        dispose();
        FuncionarioMenu menuadm = new FuncionarioMenu(usuario);
        menuadm.setVisible(true);
    }
}
