package view;

import java.awt.EventQueue;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import controller.UsuarioController;
import model.Entity.Usuario;
import model.Repository.UsuarioRepository;
import model.Service.UsuarioService;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class EditUser extends JFrame {
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
    private JButton saveButton;
    private JButton cancelButton;
    
    private Usuario usuario; // Para armazenar o usuário a ser editado

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    EditUser frame = new EditUser(1L); // Supondo que estamos editando o usuário com ID 1
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
    public EditUser(Long userId) {
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
        
        saveButton = new JButton("Salvar");
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                salvarAlteracoes();
            	dispose();
            }
        });
        saveButton.setBounds(236, 216, 85, 21);
        contentPane.add(saveButton);
        
        cancelButton = new JButton("Cancelar");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cancelarEdicao();
            }
        });
        cancelButton.setBounds(30, 216, 85, 21);
        contentPane.add(cancelButton);

        carregarUsuario(userId);
    }

    private void carregarUsuario(Long userId) {
        usuario = em.find(Usuario.class, userId);
        if (usuario != null) {
            nomeField.setText(usuario.getNome());
            senhaField.setText(usuario.getSenha());
            tipoUsuarioCombo.setSelectedIndex(usuario.isUserRole() ? 0 : 1);
        } else {
            JOptionPane.showMessageDialog(this, "Usuário não encontrado");
        }
    }

    private void salvarAlteracoes() {
        String nome = nomeField.getText();
        String senha = new String(senhaField.getPassword());
        boolean isAdmin = tipoUsuarioCombo.getSelectedIndex() == 0;

        usuario.setNome(nome);
        usuario.setSenha(senha);
        usuario.setUserRole(isAdmin);

        try {
            
            usuarioController.updateUser(usuario);
  
            JOptionPane.showMessageDialog(this, "Usuário atualizado com sucesso.");
            dispose();
        } catch (Exception ex) {
            
            JOptionPane.showMessageDialog(this, "Erro ao atualizar usuário: " + ex.getMessage());
        }
    
    }

    private void cancelarEdicao() {
        dispose();
    }
}
