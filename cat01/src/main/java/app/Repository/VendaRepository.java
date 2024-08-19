package app.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import app.Entity.Venda;

public interface VendaRepository extends JpaRepository<Venda, Long >{
	
	public Boolean existsByNfe(long numNfe);
}
