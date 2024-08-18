package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import controller.ProdutoController;
import model.Entity.Produtos;
import model.Repository.ProdutoRepository;
import model.Service.ProdutoService;

public class CadastroProduto extends JDialog {

    private JTextField nomeField;
    private JTextField precoField;
    private JTextField descricaoField;
    private ProdutoController produtoController;
    private JPanel contentPane;

    public CadastroProduto() {
        setTitle("Cadastrar Produto");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 400, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNome = new JLabel("Nome:");
        lblNome.setBounds(30, 30, 60, 20);
        contentPane.add(lblNome);

        nomeField = new JTextField();
        nomeField.setBounds(100, 30, 200, 20);
        contentPane.add(nomeField);
        nomeField.setColumns(10);

        JLabel lblPreco = new JLabel("Preço:");
        lblPreco.setBounds(30, 70, 60, 20);
        contentPane.add(lblPreco);

        precoField = new JTextField();
        precoField.setBounds(100, 70, 200, 20);
        contentPane.add(precoField);
        precoField.setColumns(10);
        
        JLabel lblDescricao = new JLabel("Descrição:");
        lblDescricao.setBounds(30, 110, 60, 20);
        contentPane.add(lblDescricao);

        descricaoField = new JTextField();
        descricaoField.setBounds(100, 110, 200, 65);
        contentPane.add(descricaoField);
        descricaoField.setColumns(10);

        JButton btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                salvarProduto();
            }
        });
        btnCadastrar.setBounds(200, 200, 100, 25);
        contentPane.add(btnCadastrar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cancelarCadastro();
            }
        });
        btnCancelar.setBounds(80, 200, 100, 25);
        contentPane.add(btnCancelar);

        ProdutoRepository produtoRepository = new ProdutoRepository(GerenciarProdutos.getEntityManagerFactory().createEntityManager());
        ProdutoService produtoService = new ProdutoService(produtoRepository);
        produtoController = new ProdutoController(produtoService);
    }

    private void salvarProduto() {
        String nome = nomeField.getText();
        String precoStr = precoField.getText();
        String descricao = descricaoField.getText();

        if (nome.isEmpty() || precoStr.isEmpty() || descricao.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos.");
            return;
        }

        try {
            float preco = Float.parseFloat(precoStr);

            Produtos produto = new Produtos();
            produto.setNome(nome);
            produto.setPreco(preco);
            produto.setDescricao(descricao);  // Set the description

            produtoController.createProduto(produto);
            JOptionPane.showMessageDialog(this, "Produto cadastrado com sucesso.");
            dispose();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Preço inválido.");
        }
    }

    private void cancelarCadastro() {
        dispose();
    }

    private void limparCampos() {
        nomeField.setText("");
        precoField.setText("");
        descricaoField.setText("");
    }
}
