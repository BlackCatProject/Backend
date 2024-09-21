package app.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import app.Entity.Venda;

public interface VendaRepository extends JpaRepository<Venda, Long> {

	public List<Venda> findByDataBetween(LocalDateTime startDate, LocalDateTime endDate);
	public List<Venda> findByUsuarioId(long usuarioId);

}