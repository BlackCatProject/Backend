package model.Service;

import model.Entity.Usuario;
import model.Repository.UsuarioRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class UsuarioService {
    private EntityManager em;
    private UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("BancoPie");
        this.em = emf.createEntityManager();
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario createUser(Usuario usuario) {
        try {
            if (!em.getTransaction().isActive()) {
                em.getTransaction().begin();
            }
            Usuario createdUser = (Usuario) usuarioRepository.create(usuario);
            em.getTransaction().commit();
            return createdUser;
        } catch (Exception ex) {
        	System.out.println(ex.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw ex;
        }
    }

    public void deleteUser(long id) {
        try {
            if (!em.getTransaction().isActive()) {
                em.getTransaction().begin();
            }
            usuarioRepository.delete(id);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw ex;
        }
    }

    public Usuario login(String nome, String senha) {
        return usuarioRepository.login(nome, senha);
    }

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public void updateUser(Usuario usuario) {
            usuarioRepository.update(usuario);
    }

    public Usuario findUserById(long id) {
        return (Usuario) usuarioRepository.findById(id);
    }

    public Usuario findBysenha(String senha) {
        return usuarioRepository.findBysenha(senha);
    }
}
