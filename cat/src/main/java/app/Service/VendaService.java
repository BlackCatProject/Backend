package app.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Iterator;
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
	
	public String save(Venda venda) {
		venda = registrarVenda(venda);
		
		//toda vez que tiver um relacionamento @onetomany e que vc salva em cascata, precisa fazer isso pra não ficar nual a chave estrangeira da venda no produto venda
		if(venda.getProdutosVenda() != null) {
			for(int i=0; i<venda.getProdutosVenda().size(); i++) {
				venda.getProdutosVenda().get(i).setVenda(venda);
			}
		}
		
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
	
	private Venda registrarVenda(Venda venda) {
		
		List<ProdutoVenda> listTemp = new ArrayList<>();
		
		//Caso um produto esteja presente em mais de um produtoVenda, junta as quantidades de em um só produtoVenda na lista temporária
		for(int i = 0; i < venda.getProdutosVenda().size(); i++) {
			boolean encontrou = false;
			for(int j = 0; j < listTemp.size(); j++) {
				if(venda.getProdutosVenda().get(i).getProduto().getId() == listTemp.get(j).getProduto().getId()) {
					encontrou = true;
					listTemp.get(j).setQuantidade(listTemp.get(j).getQuantidade() + venda.getProdutosVenda().get(i).getQuantidade());
				}
			}
			
			if(!encontrou) {
				listTemp.add(venda.getProdutosVenda().get(i));
			}
		}
		
		venda.setProdutosVenda(listTemp);
		double valorTotal = calcularTotal(venda);
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
		
		if(venda.getDesconto() == 0) {
			return valorTotal;
		}else {
			return valorTotal * (1 - (venda.getDesconto() / 100.0));
		}
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
	
	  public List<Venda> findByData(LocalDateTime startDate, LocalDateTime endDate) {
	        return this.vendaRepository.findByDataBetween(startDate, endDate);
	    }
	  
	  public List<Venda> findByMonthAndYear(int mes, int ano) {
	        // Definindo o primeiro e o último dia do mês
	        YearMonth yearMonth = YearMonth.of(ano, mes);
	        LocalDateTime startDate = yearMonth.atDay(1).atStartOfDay();
	        LocalDateTime endDate = yearMonth.atEndOfMonth().atTime(23, 59, 59);

	        return vendaRepository.findByDataBetween(startDate, endDate);
	    }
	 
	  
	  public List<Venda> findByUsuarioId(long usuarioId) {
	        return vendaRepository.findByUsuarioId(usuarioId);
	    }
	  
}
