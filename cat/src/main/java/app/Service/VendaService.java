package app.Service;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.Entity.Produto;
import app.Entity.ProdutoVenda;
import app.Entity.Venda;
import app.Repository.VendaRepository;

@Service
public class VendaService {
	@Autowired
	private VendaRepository vendaRepository;
	
	@Autowired 
	private ProdutoService produtoService;
	
	public String save(Venda venda, int desconto) {
		venda = registrarVenda(venda, desconto);
		this.vendaRepository.save(venda);
		return"Venda salva com sucesso";
	}
	
	public String update(Venda venda, long id) {
		venda.setId(id);
		venda.setTotal(calcularTotal(venda));
		this.vendaRepository.save(venda);
		return  "Atualizada com sucesso";
	}
	
	public Venda findById(long id) {
		Optional<Venda> optional = this.vendaRepository.findById(id);
		if(optional.isPresent()) {
			return optional.get();
		}else
			return null;
	}
		
	public List<Venda> findAll(){
		return this.vendaRepository.findAll();
	}
	
	public String delete(Long id) {
		this.vendaRepository.deleteById(id);
		return "Venda deletada com sucesso";
	}
	
	private Venda registrarVenda(Venda venda, int desconto) {
		double valorTotal = calcularTotal(venda) * (desconto / 100);
		venda.setTotal(valorTotal);
		venda.setNfe(gerarNfe());
		venda.setData(LocalDateTime.now());
		
		return venda;
	}
	
	private double calcularTotal(Venda venda) {
		
		double valorTotal = 0;

		for (ProdutoVenda p : venda.getProdutosVenda()) {
			if(p.getProduto().getId() == 0) {
				this.produtoService.save(p.getProduto());
				valorTotal += p.getProduto().getPreco() * p.getQuantidade();
			}else {
				Produto produto = this.produtoService.findById(p.getProduto().getId());
				valorTotal += produto.getPreco() * p.getQuantidade();
			}
		}
			return valorTotal;
	}
	
	private long gerarNfe() {
		
		Random random = new Random();
		long numNfe;
		boolean exists = false;
		do {
			numNfe = random.nextInt(900000000) + 100000000;
			exists = vendaRepository.existsByNfe(numNfe);
		}while(exists);
		
		return numNfe;
	}
	
}
