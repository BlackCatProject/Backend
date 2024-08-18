package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

import model.Entity.Usuario;

//import javax.swing.border.EmptyBorder;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminMenuFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	//private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AdminMenuFrame frame = new AdminMenuFrame(new Usuario());
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public AdminMenuFrame(Usuario usuario) {
		getContentPane().setBackground(new Color(183, 183, 219));
		 setTitle("Menu Admin");
	        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	        setBounds(100, 100, 809, 494);
	        getContentPane().setLayout(null);
	        
	        JButton btnGerenciarUsuarios = new JButton("Gerenciar usuários");
	        btnGerenciarUsuarios.setBackground(new Color(191, 191, 223));
	        btnGerenciarUsuarios.setBounds(302, 298, 145, 46);
	        btnGerenciarUsuarios.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                gerenciarUser(usuario);
	            }
	        });
	        
	        JLabel iconCat = new JLabel("");
	        iconCat.setIcon(new ImageIcon("C:\\Users\\Cristiely\\Downloads\\download__18_-removebg-preview.png"));
	        iconCat.setBounds(115, 56, 540, 186);
	        getContentPane().add(iconCat);
	        
	      
	        getContentPane().add(btnGerenciarUsuarios);

	        JButton btnGerenciarProdutos = new JButton("Gerenciar produtos");
	        btnGerenciarProdutos.setBackground(new Color(191, 191, 223));
	        btnGerenciarProdutos.setBounds(488, 298, 145, 46);
	        btnGerenciarProdutos.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                gerenciarProdutos(usuario);
	            }
	        });
	        getContentPane().add(btnGerenciarProdutos);

	        JButton btnGerenciarVendas = new JButton("Gerenciar vendas");
	        btnGerenciarVendas.setBackground(new Color(191, 191, 223));
	        btnGerenciarVendas.setBounds(115, 298, 145, 46);
	        btnGerenciarVendas.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                gerenciarVendas(usuario);
	            }
	        });
	        getContentPane().add(btnGerenciarVendas);

	        JButton btnSair = new JButton("Sair");
	        btnSair.setBackground(new Color(191, 191, 223));
	        btnSair.setBounds(302, 368, 145, 46);
	        btnSair.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                dispose();
	                LoginFrame.main(null);
	            }
	        });
	        getContentPane().add(btnSair);
	        
	        JPanel panel = new JPanel();
	        panel.setBackground(new Color(255, 255, 255));
	        panel.setBounds(0, 222, 793, 233);
	        getContentPane().add(panel);
	        	        
	        	  JLabel lblTitle = new JLabel("Menu Administração");
	        	  lblTitle.setBounds(295, 11, 166, 20);
	        	  getContentPane().add(lblTitle);
	        	  lblTitle.setFont(new Font("Tahoma", Font.BOLD, 16));
	    }

	    private void gerenciarUser(Usuario usuario) {
	    	  new GerenciarUser(usuario).setVisible(true);
	    	  dispose();
	    }

	    private void gerenciarProdutos(Usuario usuario) {
	    	 new GerenciarProdutos(usuario).setVisible(true);
	    	  dispose();
	        
	    }

	    private void gerenciarVendas(Usuario usuario) {
	        Venda vendaFrame = new Venda(usuario); // Passe o usuário para o construtor da Venda
	        vendaFrame.setVisible(true); // Exiba o frame de Venda
	        dispose(); // Feche o frame atual (provavelmente o frame de login)
	    }

	    
}
