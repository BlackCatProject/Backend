package app.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import app.Entity.Produto;
import app.Entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
}
