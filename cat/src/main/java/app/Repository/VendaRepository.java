package app.Repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import app.Entity.Venda;

public interface VendaRepository extends JpaRepository<Venda, Long >{
	  List<Venda> findByDataBetween(LocalDateTime startDate, LocalDateTime endDate);
	public Boolean existsByNfe(long numNfe);
	
	  List<Venda> findByUsuarioId(long usuarioId);
}