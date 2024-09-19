package app.Controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import app.Entity.Venda;
import app.Service.VendaService;

@RestController
@RequestMapping("/api/venda")
public class VendaController {
	@Autowired
	private VendaService vendaService;
	

	@PostMapping("/save")
	public ResponseEntity<String> save(@RequestBody Venda venda) {
		try {
			String msn = this.vendaService.save(venda);
			return new ResponseEntity<>(msn, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Deu Erro! " + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<String> update(@RequestBody Venda venda, @PathVariable Long id) {
		try {
			String msn = this.vendaService.update(venda, id);
			return new ResponseEntity<>(msn, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Deu erro! " + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/findById/{id}")
	public ResponseEntity<Venda> findById(@PathVariable int id) {
		try {
			Venda funcionario = this.vendaService.findById(id);
			return new ResponseEntity<>(funcionario, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/findAll")
	public ResponseEntity<List<Venda>> findAll() {
		try {
			List<Venda> lista = this.vendaService.findAll();
			return new ResponseEntity<>(lista, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> delete(@PathVariable Long id) {
		try {
			String msn = this.vendaService.delete(id);
			return new ResponseEntity<>(msn, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}

	}

	
	// api/venda/findByData?startDate=2024-08-18T00:00:00&endDate=2024-08-18T23:59:59
	@GetMapping("/findByData")
	public ResponseEntity<List<Venda>> findByDataBetween(
// indica que startDate e endDate será extraído da URL da solicitação.
			@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
			@RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
		if (startDate.isAfter(endDate)) {
			return ResponseEntity.badRequest().body(null);// Garante que startDate não seja depois de endDate.
		}
		try {
			List<Venda> vendas = vendaService.findByData(startDate, endDate);
			return ResponseEntity.ok(vendas);
		} catch (DateTimeParseException e) {
			return ResponseEntity.badRequest().body(null);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	// exemplo pra buscar -> api/venda/findByMonth/2024/08
	@GetMapping("/findByMonth/{ano}/{mes}")
	public ResponseEntity<List<Venda>> findByMonthAndYear(@PathVariable int ano, @PathVariable int mes) {
		try {
			List<Venda> vendas = vendaService.findByMonthAndYear(mes, ano);
			return new ResponseEntity<>(vendas, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/findByUsuario/{usuarioId}")
	public ResponseEntity<List<Venda>> findByUsuarioId(@PathVariable("usuarioId") long usuarioId) {
		try {
			List<Venda> vendas = vendaService.findByUsuarioId(usuarioId);
			if (vendas.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(vendas, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/buscar-por-nfe")
	public ResponseEntity<Venda> buscarPorNumeroNfe(@RequestParam long nfe) {
		Venda venda = vendaService.buscarPorNumeroNfe(nfe);
		return ResponseEntity.ok(venda);
	}

}
