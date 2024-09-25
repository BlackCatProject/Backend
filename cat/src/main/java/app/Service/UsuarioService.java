package app.Service;

import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.Entity.Usuario;
import app.Repository.UsuarioRepository;
import org.springframework.validation.annotation.Validated;

@Service
public class UsuarioService {
	@Autowired
	private UsuarioRepository usuarioRepository;

	public String save(Usuario usuario) {

		if (conferirUser(usuario)) {
			throw new RuntimeException("Login ou Senha já está em uso");
		}

		usuario.setAtivo(true);
		this.usuarioRepository.save(usuario);
		return "Usuário salvo com sucesso";
	}

	public String update(Usuario usuario, long id) {
		 // Define o ID no objeto usuário antes de verificar a existência de login duplicado
	    usuario.setId(id);
	    
	    if (conferirUser(usuario)) {
	        throw new RuntimeException("Login ou Senha já está em uso");
	    }

		this.usuarioRepository.save(usuario);
		return "Atualizado com sucesso";
	}

	public boolean conferirUser(Usuario usuario) {
		Optional<Usuario> existingUser = usuarioRepository.findByLogin(usuario.getLogin());
		// Verifica se o usuário existe e se o ID é diferente do ID do usuário que está sendo atualizado
		return existingUser.isPresent() && existingUser.get().getId() != usuario.getId();
	}

	public Usuario findById(long id) {
		Optional<Usuario> optional = this.usuarioRepository.findById(id);
		if (optional.isPresent()) {
			return optional.get();
		} else
			throw new RuntimeException("Usúario não encontrado");
	}

	public List<Usuario> findAll(boolean ativo) {
		return this.usuarioRepository.findByAtivo(ativo);
	}

	public String delete(Long id) {
		this.usuarioRepository.deleteById(id);
		return "Usuario deletado com sucesso";
	}
	
	public String disable(Long id) {
		Usuario usuarioInDB = this.findById(id);
		usuarioInDB.setAtivo(false);
		this.usuarioRepository.save(usuarioInDB);
		return "Usuário desativado com sucesso!";
	}
	
	public String enable(Long id) {
		Usuario usuarioInDB = this.findById(id);
		usuarioInDB.setAtivo(true);
		this.usuarioRepository.save(usuarioInDB);
		return "Usuário ativado com sucesso!";
	}

}
