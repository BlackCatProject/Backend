package app.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import app.Entity.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long>{
}
