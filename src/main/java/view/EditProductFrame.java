package view;

import model.Entity.Produtos;
import model.Entity.Usuario;
import model.Repository.ProdutoRepository;
import model.Repository.VendaRepository;
import model.Service.ProdutoService;
import model.Service.VendaService;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import controller.ProdutoController;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditProductFrame extends JFrame {

	private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("BancoPie");
	private static EntityManager em = emf.createEntityManager();

	private static ProdutoRepository produtoRepository = new ProdutoRepository(em);
	private static ProdutoService produtoService = new ProdutoService(produtoRepository);
	private static ProdutoController produtoController = new ProdutoController(produtoService);

	private static VendaRepository vendaRepository = new VendaRepository(em);
	private static VendaService vendaService = new VendaService(vendaRepository);
	private static Usuario usuarioLogado;

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField nameField;
	private JTextField descriptionField;
	private JTextField priceField;
	private Produtos produto;
	private ListProductsFrame parentFrame;

	/**
	 * Create the frame.
	 */
	public EditProductFrame(Produtos produto) {
		this.produto = produto;
		this.parentFrame = parentFrame;
		setTitle("Editar Produto");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblName = new JLabel("Nome:");
		lblName.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblName.setBounds(50, 30, 80, 25);
		contentPane.add(lblName);

		nameField = new JTextField();
		nameField.setBounds(140, 31, 200, 25);
		contentPane.add(nameField);
		nameField.setColumns(10);
		nameField.setText(produto.getNome());

		JLabel lblDescription = new JLabel("Descrição:");
		lblDescription.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDescription.setBounds(50, 70, 80, 25);
		contentPane.add(lblDescription);

		descriptionField = new JTextField();
		descriptionField.setBounds(140, 70, 200, 25);
		contentPane.add(descriptionField);
		descriptionField.setColumns(10);
		descriptionField.setText(produto.getDescricao());

		JLabel lblPrice = new JLabel("Preço:");
		lblPrice.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPrice.setBounds(50, 110, 80, 25);
		contentPane.add(lblPrice);

		priceField = new JTextField();
		priceField.setBounds(140, 110, 200, 25);
		contentPane.add(priceField);
		priceField.setColumns(10);
		priceField.setText(String.valueOf(produto.getPreco()));

		JButton btnSave = new JButton("Salvar");
		btnSave.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnSave.setBounds(240, 200, 100, 30);
		contentPane.add(btnSave);
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveProduct();
				dispose();
			}
		});

		JButton btnCancel = new JButton("Cancelar");
		btnCancel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnCancel.setBounds(130, 200, 100, 30);
		contentPane.add(btnCancel);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}

	private void saveProduct() {
		String nome = nameField.getText();
		String descricao = descriptionField.getText();
		float preco = Float.parseFloat(priceField.getText());

		produto.setNome(nome);
		produto.setDescricao(descricao);
		produto.setPreco(preco);

		try {
			produtoController.updateProduto(produto);
			
			produto = produtoController.findProdutoByid(produto.getId());
			if(produto != null) {
				JOptionPane.showMessageDialog(this, "Produto atualizado com sucesso.");
			}else {
				JOptionPane.showMessageDialog(this, "Erro ao atualizar produto");
			}
			
		} catch (Exception ex) {

			JOptionPane.showMessageDialog(this, "Erro ao atualizar Produto: " + ex.getMessage());
		}

	}
}
