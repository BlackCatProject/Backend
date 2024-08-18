package view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import controller.UsuarioController;
import model.Entity.Usuario;
import model.Repository.UsuarioRepository;
import model.Service.UsuarioService;


public class LoginFrame extends JFrame {
	 private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("BancoPie");
	 private static EntityManager em = emf.createEntityManager();

	 private static UsuarioRepository usuarioRepository = new UsuarioRepository(em);
	 private static UsuarioService usuarioService = new UsuarioService(usuarioRepository);
	 private static UsuarioController usuarioController = new UsuarioController(usuarioService);

    private static final long serialVersionUID = 1L;
    private JTextField nometext;
    private JPasswordField jtxtpassward;
    //private 

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    LoginFrame frame = new LoginFrame();
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
    public LoginFrame() {
    	getContentPane().setBackground(new Color(255, 255, 255));
    	 setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         setBounds(100, 100, 809, 494);
         getContentPane().setLayout(null);

         JLabel cat = new JLabel("");
         cat.setBounds(266, 0, 393, 467);
         getContentPane().add(cat);
         cat.setIcon(new ImageIcon("C:\\Users\\Cristiely\\Downloads\\image-removebg-preview (35).png"));

         JPanel panel = new JPanel();
         panel.setBackground(new Color(193, 193, 255));
         panel.setBounds(0, 0, 467, 464);
         getContentPane().add(panel);
         panel.setLayout(null);

         JLabel nomeLabel = new JLabel("Nome");
         nomeLabel.setBackground(new Color(0, 0, 0));
         nomeLabel.setBounds(55, 177, 70, 14);
         panel.add(nomeLabel);

         JLabel senhaLabel = new JLabel("Senha");
         senhaLabel.setBounds(55, 225, 70, 14);
         panel.add(senhaLabel);

         nometext = new JTextField();
         nometext.setBounds(96, 168, 206, 32);
         panel.add(nometext);
         nometext.setColumns(10);

         JButton buttonLogin = new JButton("Login");
         buttonLogin.setFont(new Font("Lucida Sans", Font.BOLD, 14));
         buttonLogin.setForeground(Color.WHITE);
         buttonLogin.setBackground(Color.BLACK);
         buttonLogin.setOpaque(true);
         buttonLogin.setBorderPainted(false);
         buttonLogin.setContentAreaFilled(true);
         buttonLogin.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
                 String nome = nometext.getText();
                 char[] passwordChars = jtxtpassward.getPassword();
                 String senha = new String(passwordChars);

                 Usuario usuarioLogado = usuarioController.login(nome, senha);
                 java.util.Arrays.fill(passwordChars, ' ');

                 if (usuarioLogado != null) {
                     System.out.println("Bem-Vind@, " + usuarioLogado.getNome() + "!");
                     if (usuarioLogado.isUserRole()) {
                         new AdminMenuFrame(usuarioLogado).setVisible(true);
                     } else {
                         new FuncionarioMenu(usuarioLogado).setVisible(true);
                     }
                     dispose(); // fecha o login 
                 } else {
                     JOptionPane.showMessageDialog(LoginFrame.this,
                             "Nome de usuário ou senha incorretos. Por favor, tente novamente.",
                             "Erro de Login",
                             JOptionPane.ERROR_MESSAGE);
                     limparCampos();
                 }
             }
         });
         buttonLogin.setBounds(138, 337, 119, 32);
         panel.add(buttonLogin);

         JLabel titulo = new JLabel("BlackCat");
         titulo.setFont(new Font("Ravie", Font.BOLD, 30));
         titulo.setBounds(96, 79, 232, 82);
         panel.add(titulo);

         jtxtpassward = new JPasswordField();
         jtxtpassward.setBounds(96, 216, 206, 32);
         panel.add(jtxtpassward);
     }
    
    private void limparCampos() {
        nometext.setText("");
        jtxtpassward.setText("");
    }
    
}
