package view;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.*;

//import controller.UsuarioController;

import java.awt.*;
import java.util.List;
import model.Entity.Usuario;
import model.Repository.UsuarioRepository;
//import model.Service.UsuarioService;

public class ListProductsFrame extends JFrame {
	

	 private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("BancoPie");
	 private static EntityManager em = emf.createEntityManager();

	 private static UsuarioRepository usuarioRepository = new UsuarioRepository(em);
	// private static UsuarioService usuarioService = new UsuarioService(usuarioRepository);
	// private static UsuarioController usuarioController = new UsuarioController(usuarioService);

  public ListProductsFrame() {
      initComponents();
  }

  private void initComponents() {
      setTitle("Lista de Usuários");
      setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      setSize(400, 300);
      setLocationRelativeTo(null);

      JPanel contentPane = new JPanel(new BorderLayout());

      List<Usuario> usuarios = usuarioRepository.findAll();
      if (usuarios.isEmpty()) {
          contentPane.add(new JLabel("Não há usuários cadastrados no sistema."), BorderLayout.CENTER);
      } else {
          JPanel usuariosPanel = new JPanel(new GridLayout(usuarios.size(), 1));
          for (Usuario usuario : usuarios) {
              JPanel usuarioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
              usuarioPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
              usuarioPanel.add(new JLabel("Id: " + usuario.getId()));
              usuarioPanel.add(new JLabel("Nome: " + usuario.getNome()));

              usuariosPanel.add(usuarioPanel);
          }
          contentPane.add(new JScrollPane(usuariosPanel), BorderLayout.CENTER);
      }

      setContentPane(contentPane);
  }

  public static void main(String[] args) {
      SwingUtilities.invokeLater(() -> {
          ListProductsFrame frame = new ListProductsFrame();
          frame.setVisible(true);
      });
  }
}

