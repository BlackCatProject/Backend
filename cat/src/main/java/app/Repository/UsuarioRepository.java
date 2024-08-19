package app.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import app.Entity.Produto;
import app.Entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
	 Optional<Usuario> findByLogin(String login);
	    Optional<Usuario> findByNomeAndSenha(String nome, String senha);
}
