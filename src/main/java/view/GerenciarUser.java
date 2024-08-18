package view;

import model.Entity.Usuario;
import model.Repository.UsuarioRepository;
import model.Service.UsuarioService;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
//import javax.swing.JTextField;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

import controller.UsuarioController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.util.ArrayList;
import java.util.List;

public class GerenciarUser extends JFrame {
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("BancoPie");
    private static EntityManager em = emf.createEntityManager();

    private static UsuarioRepository usuarioRepository = new UsuarioRepository(em);
    private static UsuarioService usuarioService = new UsuarioService(usuarioRepository);
    private static UsuarioController usuarioController = new UsuarioController(usuarioService);

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private DefaultTableModel tableModel;
   // private ArrayList<Usuario> usuarios = new ArrayList<>();

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    GerenciarUser frame = new GerenciarUser(new Usuario());
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public GerenciarUser(Usuario usuario) {
        setTitle("Gerenciar Funcionários");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 735, 489);

        contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 255, 255));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JButton btnCreate = new JButton("Cadastrar Funcionário");
        btnCreate.setBackground(new Color(197, 197, 226));
        btnCreate.setBounds(467, 413, 178, 30);
        contentPane.add(btnCreate);
        btnCreate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showCadastroFuncionarioDialog();
            }
        });

        JButton btnList = new JButton("Atualizar lista");
        btnList.setBackground(new Color(197, 197, 226));
        btnList.setBounds(469, 11, 117, 25);
        contentPane.add(btnList);
        btnList.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                listAllUsuarios();
            }
        });

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(20, 47, 666, 270);
        contentPane.add(scrollPane);

        table = new JTable();
        scrollPane.setViewportView(table);

        tableModel = new DefaultTableModel(
            new Object[][] {},
            new String[] {"ID", "Nome"}
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
                    JOptionPane.showMessageDialog(GerenciarUser.this, "Por favor, selecione um usuário para editar.");
                    return;
                }
                Long userId = (Long) table.getValueAt(selectedRow, 0);
                EditUsuario(userId);
               
            }
            
        });
        btnEditar.setBackground(new Color(197, 197, 226));
        btnEditar.setBounds(369, 11, 90, 25);
        contentPane.add(btnEditar);
        
        JLabel lblNewLabel = new JLabel("Gestão De Funcionarios");
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblNewLabel.setBounds(88, 15, 205, 13);
        contentPane.add(lblNewLabel);
        
        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteUsuario();
            }
        });
        listAllUsuarios();
    }
    

    private void showCadastroFuncionarioDialog() {
        CadastroUser dialog = new CadastroUser();
        dialog.setVisible(true);
      
    }

    private void listAllUsuarios() {
        tableModel.setRowCount(0);
        List<Usuario> usuarios = usuarioRepository.findAll();
        for (Usuario usuario : usuarios) {
            Object[] rowData = {usuario.getId(), usuario.getNome()};
            tableModel.addRow(rowData);
        }
    }

    private void EditUsuario(Long userId) {
        EditUser dialog = new EditUser(userId);    
        dialog.setVisible(true);
  
    }

    private void voltarMenuAdm(Usuario usuario) {
        dispose();
        AdminMenuFrame menuadm = new AdminMenuFrame(usuario);
        menuadm.setVisible(true);
    }

    private void deleteUsuario() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione um usuário para excluir.");
            return;
        }

        long userId = (long) table.getValueAt(selectedRow, 0);
        int option = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir este usuário?",
                                                    "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
        if (option != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            usuarioController.deleteUserById(userId);
            JOptionPane.showMessageDialog(this, "Usuário excluído com sucesso.");
            listAllUsuarios();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao excluir usuário: " + ex.getMessage());
        }
    }
    
}
