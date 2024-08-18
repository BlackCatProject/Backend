package view;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import model.Entity.Vendas;
import model.Repository.VendaRepository;
import model.Service.VendaService;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ConsultaVendas extends JFrame {
	 private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("BancoPie");
	    private static EntityManager em = emf.createEntityManager();

    private VendaRepository vendaRepository;
    private VendaService vendaService;

    private JTable table;
    private DefaultTableModel tableModel;

    public ConsultaVendas(VendaRepository vendaRepository, VendaService vendaService) {
        this.vendaRepository = vendaRepository;
        this.vendaService = vendaService;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(5, 28, 774, 528);
        contentPane.add(scrollPane);

        table = new JTable();
        scrollPane.setViewportView(table);

        tableModel = new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Data", "Total", "Número NFe", "Itens"}
        );
        table.setModel(tableModel);

        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.setBounds(5, 5, 774, 23);
        btnAtualizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                carregarVendas();
            }
        });
        contentPane.add(btnAtualizar);

        carregarVendas(); // Carregar vendas ao abrir o frame
    }

    private void carregarVendas() {
        List<Vendas> vendas = vendaService.findAll();

        tableModel.setRowCount(0); // Limpar tabela antes de adicionar novos dados

        for (Vendas venda : vendas) {
            Object[] rowData = {
                    venda.getId(),
                    venda.getDataVenda(),
                    venda.getTotal(), 
                    venda.getNfe(),
                    venda.getItens().size() // Supondo que produtos seja uma coleção em Vendas
            };
            tableModel.addRow(rowData);
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
              
                VendaRepository vendaRepository = new VendaRepository( em);
                VendaService vendaService = new VendaService(vendaRepository);

                ConsultaVendas frame = new ConsultaVendas(vendaRepository, vendaService);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
