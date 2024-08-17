package app.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.Entity.Usuario;
import app.Repository.UsuarioRepository;

@Service
public class UsuarioService {
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	private String save(Usuario usuario) {
		this.usuarioRepository.save(usuario);
		return"Usuario salvo com sucesso";
	}
	
	private String update(Usuario usuario, long id) {
		usuario.setId(id);
		this.usuarioRepository.save(usuario);
		return  "Atualizado com sucesso";
	}
	
	private Usuario findById(long id) {
		Optional<Usuario> optional = this.usuarioRepository.findById(id);
		if(optional.isPresent()) {
			return optional.get();
		}else
			return null;
	}
		
	private List<Usuario> findAll(){
		return this.usuarioRepository.findAll();
	}
	
	private String delete(Long id) {
		this.usuarioRepository.deleteById(id);
		return "Usuario deletado com sucesso";
	}
	
}
