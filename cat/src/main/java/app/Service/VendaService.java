package app.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.Entity.Usuario;
import app.Entity.Venda;
import app.Repository.UsuarioRepository;
import app.Repository.VendaRepository;

@Service
public class VendaService {
	@Autowired
	private VendaRepository vendaRepository;
	
	private String save(Venda venda) {
		this.vendaRepository.save(venda);
		return"Usuario salvo com sucesso";
	}
	
	private String update(Venda venda, long id) {
		venda.setId(id);
		this.vendaRepository.save(venda);
		return  "Atualizado com sucesso";
	}
	
	private Venda findById(long id) {
		Optional<Venda> optional = this.vendaRepository.findById(id);
		if(optional.isPresent()) {
			return optional.get();
		}else
			return null;
	}
		
	private List<Venda> findAll(){
		return this.vendaRepository.findAll();
	}
	
	private String delete(Long id) {
		this.vendaRepository.deleteById(id);
		return "Venda deletada com sucesso";
	}
	
	
}
