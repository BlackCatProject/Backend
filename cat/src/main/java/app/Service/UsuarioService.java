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

	public String save(Usuario usuario) {

		if (conferirUser(usuario)) {
			throw new RuntimeException("Login ou Senha já está em uso");
		}

		usuario.setAtivo(true);
		this.usuarioRepository.save(usuario);
		return "Usuário salvo com sucesso";
	}

	public String update(Usuario usuario, long id) {
		if (conferirUser(usuario)) {
			throw new RuntimeException("Login ou Senha já está em uso");
		}

		usuario.setId(id);
		this.usuarioRepository.save(usuario);
		return "Atualizado com sucesso";
	}

	public boolean conferirUser(Usuario usuario) {
		Optional<Usuario> existingUser = usuarioRepository.findByLogin(usuario.getLogin());
		return existingUser.isPresent();
	}

	public Usuario findById(long id) {
		Optional<Usuario> optional = this.usuarioRepository.findById(id);
		if (optional.isPresent()) {
			return optional.get();
		} else
			return null;
	}

	public List<Usuario> findAll() {
		return this.usuarioRepository.findAll();
	}

	public String delete(Long id) {
		this.usuarioRepository.deleteById(id);
		return "Usuario deletado com sucesso";
	}
	
	public String disable(Long id) {
		Usuario usuarioInDB = this.findById(id);
		usuarioInDB.setAtivo(false);
		this.usuarioRepository.save(usuarioInDB);
		return " Usuário desativado com sucesso!";
	}
	
	public String enable(Long id) {
		Usuario usuarioInDB = this.findById(id);
		usuarioInDB.setAtivo(true);
		this.usuarioRepository.save(usuarioInDB);
		return " Usuário desativado com sucesso!";
	}

}
