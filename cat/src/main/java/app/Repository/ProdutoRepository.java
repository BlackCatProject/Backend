package app.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import app.Entity.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
	
	public boolean existsByNomeAndDescricao(String nome, String descricao);
	
	public List<Produto> findByAtivo(boolean ativo);
	
}
