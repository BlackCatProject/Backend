package app.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.Entity.Produto;
import app.Repository.ProdutoRepository;

@Service
public class ProdutoService {
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	private String save( Produto produto) {
		this.produtoRepository.save(produto);
		return "Produto salvo com sucesso";
	}
	
	private String update(Produto produto, long id) {
		produto.setId(id);
		this.produtoRepository.save(produto);
		return  "Atualizado com sucesso";
	}
	
	private Produto findById(long id) {
		Optional<Produto> optional = this.produtoRepository.findById(id);
		if(optional.isPresent()) {
			return optional.get();
		}else
			return null;
		}
	
	private List<Produto> findAll(){
		return this.produtoRepository.findAll();
	}
	
	private String delete(Long id) {
		this.produtoRepository.deleteById(id);
		return " Produto deletado com sucesso!";
	}
	
}

