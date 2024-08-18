package model.Service;

import model.Entity.ProdutoHasVenda;
import model.Entity.Usuario;
import model.Entity.Vendas;
import model.Repository.VendaRepository;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.util.List;

public class VendaService {
   // private EntityManager em;
    private VendaRepository vendaRepository;

    public  VendaService(VendaRepository vendaRepository){
        //this.em = Persistence.createEntityManagerFactory("BancoPie").createEntityManager();
        this.vendaRepository = vendaRepository;
    }
    //criar venda
    public Vendas createvenda(Vendas vendas){
        return (Vendas) vendaRepository.create(vendas);
    }

    //deletar venda
    public  void deletevenda(long id){
        vendaRepository.delete(id);
    }

    //editar usuario
    public  void updateVenda(Vendas vendas){
        vendaRepository.update(vendas);
    }

    //listar
    public List<Vendas> findAll(){
        return vendaRepository.findALl();
    }

    //buscar por id
    public Vendas findVendaById(long id) {
        return (Vendas) vendaRepository.findById(id);
    }

    public void saveVenda(Vendas venda) {
        vendaRepository.save(venda);
    }

    public ProdutoHasVenda saveProdutoHasVenda(ProdutoHasVenda produtoHasVenda) {
        return vendaRepository.saveProdutoHasVenda(produtoHasVenda);
    }

    public Vendas registrarVenda(Vendas venda, Long idUsuario) {
        
        if (venda.getUsuario() == null || venda.getUsuario().getId() != idUsuario) {
            Usuario usuario = new Usuario();
            usuario.setId(idUsuario); 
            venda.setUsuario(usuario);
        }

        try {
            return (Vendas) vendaRepository.create(venda); 
         
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao registrar venda: " + e.getMessage());
           
            throw new RuntimeException("Erro ao registrar venda", e);
        }
    }
}
