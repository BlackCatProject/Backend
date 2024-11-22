package app.Service;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;  // Corrigido para a importação correta do Spring

import app.auth.Usuario;
import app.Repository.UsuarioRepository;

@Service
public class UsuarioService {
	@Autowired
	private UsuarioRepository usuarioRepository;

 @Autowired
 private BCryptPasswordEncoder bCryptPasswordEncoder;

	public String save(Usuario usuario) {

		if (conferirUser(usuario)) {
			throw new RuntimeException("Login ou Senha já está em uso");
		}


		usuario.setAtivo(true);
		usuario.setSenha(this.bCryptPasswordEncoder.encode(usuario.getSenha()));
		this.usuarioRepository.save(usuario);
		return "Usuário salvo com sucesso";
	}

	public String update(Usuario usuario, long id) {
		 // Define o ID no objeto usuário antes de verificar a existência de login duplicado
	    usuario.setId(id);
	    
	    if (conferirUser(usuario)) {
	        throw new RuntimeException("Login ou Senha já está em uso");
	    }
	    Optional<Usuario> optional = this.usuarioRepository.findById(id);
		if (optional.isPresent()) {
			Usuario userInDB = findById(id);
			if(!bCryptPasswordEncoder.matches(usuario.getSenha(),userInDB.getSenha())) {
		    	usuario.setSenha(this.bCryptPasswordEncoder.encode(usuario.getSenha()));
		    }
			this.usuarioRepository.save(usuario);
			System.out.println("Chegou aqui");
			return "Atualizado com sucesso";
		}else {
			throw new RuntimeException("Usúario não encontrado");
		}
	    
	}

	public boolean conferirUser(Usuario usuario) {
		Optional<Usuario> existingUser = usuarioRepository.findByLogin(usuario.getLogin());
		// Verifica se o usuário existe e se o ID é diferente do ID do usuário que está sendo atualizado
		
		return existingUser.isPresent() && existingUser.get().getId() != usuario.getId();
	}
	

	public Usuario findById(long id) {
		Optional<Usuario> optional = this.usuarioRepository.findById(id);
		if (optional.isPresent()) {
			optional.get().setSenha("");
			return optional.get();
		}else
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
		Optional<Usuario> optional = this.usuarioRepository.findById(id);
		if(optional.isPresent()) {
			Usuario usuarioInDB = optional.get();
			usuarioInDB.setAtivo(false);
			this.usuarioRepository.save(usuarioInDB);
			return "Usuário desativado com sucesso!";
		}else{
			throw new RuntimeException("Usúario não encontrado");
		}
	}
	
	public String enable(Long id) {
		Optional<Usuario> optional = this.usuarioRepository.findById(id);
		if(optional.isPresent()) {
			Usuario usuarioInDB = optional.get();
			usuarioInDB.setAtivo(true);
			this.usuarioRepository.save(usuarioInDB);
			return "Usuário ativado com sucesso!";
		}else{
			throw new RuntimeException("Usúario não encontrado");
		}	
	}

	 public Usuario getUsuarioLogado() {
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        String username = authentication.getName(); // Aqui assumimos que o login é o username
	        return usuarioRepository.findByLogin(username)
	                .orElseThrow(() -> new RuntimeException("Usuário logado não encontrado"));
	    }
}
