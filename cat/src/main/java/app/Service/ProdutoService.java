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

	public String save(Produto produto) {

		if (conferirProd(produto)) {
			throw new RuntimeException("Produto com o mesmo nome e descrição já existe");
		}

		this.produtoRepository.save(produto);
		return "Produto salvo com sucesso";
	}

	public String update(Produto produto, long id) {

		if (conferirProd(produto)) {
			throw new RuntimeException("Produto com o mesmo nome e descrição já existe");
		}
		produto.setId(id);
		this.produtoRepository.save(produto);
		return "Atualizado com sucesso";
	}

	public boolean conferirProd(Produto produto) {
		
		return this.produtoRepository.existsByNomeAndDescricao(produto.getNome(), produto.getDescricao());
		
	}

	public Produto findById(long id) {
		Optional<Produto> optional = this.produtoRepository.findById(id);
		if (optional.isPresent()) {
			return optional.get();
		} else
			return null;
	}

	public List<Produto> findAll() {
		return this.produtoRepository.findAll();
	}

	public String delete(Long id) {
		this.produtoRepository.deleteById(id);
		return " Produto deletado com sucesso!";
	}

}
