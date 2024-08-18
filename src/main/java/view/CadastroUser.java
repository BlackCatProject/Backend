package view;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import controller.UsuarioController;
import model.Entity.Usuario;
import model.Repository.UsuarioRepository;
import model.Service.UsuarioService;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CadastroUser extends JFrame {
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("BancoPie");
    private static EntityManager em = emf.createEntityManager();

    private static UsuarioRepository usuarioRepository = new UsuarioRepository(em);
    private static UsuarioService usuarioService = new UsuarioService(usuarioRepository);
    private static UsuarioController usuarioController = new UsuarioController(usuarioService);

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField nomeField;
    private JPasswordField senhaField;
    private JComboBox<String> tipoUsuarioCombo;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (em.isOpen()) {
                em.close();
            }
            if (emf.isOpen()) {
                emf.close();
            }
        }));

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    CadastroUser frame = new CadastroUser();
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
    public CadastroUser() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

        JLabel lblSenha = new JLabel("Senha:");
        lblSenha.setBounds(30, 70, 60, 20);
        contentPane.add(lblSenha);

        senhaField = new JPasswordField();
        senhaField.setBounds(100, 70, 200, 20);
        contentPane.add(senhaField);

        JLabel lblTipoDeUsuario = new JLabel("Tipo de Usuário:");
        lblTipoDeUsuario.setBounds(30, 110, 100, 20);
        contentPane.add(lblTipoDeUsuario);

        tipoUsuarioCombo = new JComboBox<>();
        tipoUsuarioCombo.setModel(new DefaultComboBoxModel<>(new String[]{"Administrador", "Funcionário"}));
        tipoUsuarioCombo.setBounds(140, 110, 160, 20);
        contentPane.add(tipoUsuarioCombo);

        JButton btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nome = nomeField.getText();
                String senha = new String(senhaField.getPassword());
                boolean isAdmin = tipoUsuarioCombo.getSelectedIndex() == 0;

                Usuario novoUsuario = new Usuario();
                novoUsuario.setNome(nome);
                novoUsuario.setSenha(senha);
                novoUsuario.setUserRole(isAdmin);

                try {
                    if (!em.getTransaction().isActive()) {
                        //em.getTransaction().begin();
                    }
                    usuarioController.createUser(novoUsuario);
                    //em.getTransaction().commit();

                    JOptionPane.showMessageDialog(CadastroUser.this, "Novo usuário criado com sucesso.");
                } catch (Exception ex) {
                    ex.printStackTrace(); // Log the stack trace
                    if (em.getTransaction().isActive()) {
                        em.getTransaction().rollback();
                    }
                    JOptionPane.showMessageDialog(CadastroUser.this, "Erro ao criar novo usuário: " + ex.getMessage());
                }
                limparCampos();
                dispose();
            }
        });
        btnCadastrar.setBounds(250, 216, 100, 25);
        contentPane.add(btnCadastrar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cancelarCadastro();
            }
        });
        btnCancelar.setBounds(30, 216, 100, 25);
        contentPane.add(btnCancelar);
    }

    private void cancelarCadastro() {
        dispose();
    }

    private void limparCampos() {
        nomeField.setText("");
        senhaField.setText("");
        tipoUsuarioCombo.setSelectedIndex(0);
    }
}
