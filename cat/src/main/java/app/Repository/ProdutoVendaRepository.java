package app.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import app.Entity.ProdutoVenda;

public interface ProdutoVendaRepository extends JpaRepository<ProdutoVenda, Long> {

}