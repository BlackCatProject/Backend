package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CadastroFuncionarioDialog extends JDialog {
    private JTextField nomeField;
    private JTextField cargoField;
    private JButton btnSalvar;
    private JButton btnCancelar;

    public CadastroFuncionarioDialog(JFrame parent) {
        super(parent, "Cadastrar Funcionário", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(parent);

        JPanel panel = new JPanel(new GridLayout(3, 2));
        getContentPane().add(panel);

        panel.add(new JLabel("Nome:"));
        nomeField = new JTextField();
        panel.add(nomeField);

        panel.add(new JLabel("Cargo:"));
        cargoField = new JTextField();
        panel.add(cargoField);

        btnSalvar = new JButton("Salvar");
        btnCancelar = new JButton("Cancelar");

        panel.add(btnSalvar);
        panel.add(btnCancelar);

        btnSalvar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Aqui você pode chamar o método para salvar o funcionário
                // por exemplo: salvarFuncionario();
                dispose(); // fecha o diálogo após salvar
            }
        });

        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // fecha o diálogo sem salvar
            }
        });
    }
}
