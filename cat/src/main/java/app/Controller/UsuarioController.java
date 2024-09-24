package app.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import app.Entity.Usuario;
import app.Service.UsuarioService;

@RestController
@RequestMapping("api/usuario")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;

	 @PostMapping("/save")
	    public ResponseEntity<String> save(@RequestBody Usuario usuario) {
	        try {
	            String message = this.usuarioService.save(usuario);
	            if (message.contains("salvo")) {
	                return new ResponseEntity<>(message, HttpStatus.OK);
	            } else {
	                return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
	            }
	        } catch (Exception e) {
	            return new ResponseEntity<>("Erro: " + e.getMessage(), HttpStatus.BAD_REQUEST);
	        }
	    }

	@PutMapping("/update/{id}")
	public ResponseEntity<String> update(@RequestBody Usuario usuario, @PathVariable Long id) {
		try {
			String msn = this.usuarioService.update(usuario, id);
			return new ResponseEntity<>(msn, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Erro ao atualizar usuário! " + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/findById/{id}")
	public ResponseEntity<Usuario> findByIndex(@PathVariable long id) {
		try {
			Usuario funcionario = this.usuarioService.findById(id);
			return new ResponseEntity<>(funcionario, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/findAll")
	public ResponseEntity<List<Usuario>> findAll(@RequestParam boolean ativo) {
		try {
			List<Usuario> lista = this.usuarioService.findAll(ativo);
			return new ResponseEntity<>(lista, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> delete(@PathVariable Long id) {
		try {
			String msn = this.usuarioService.delete(id);
			return new ResponseEntity<>(msn, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Erro ao deletar Usuario!", HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping("/disable/{id}")
    public ResponseEntity<String> disable(@PathVariable Long id) {
        try {
            String msn = this.usuarioService.disable(id);
            return new ResponseEntity<>(msn, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Deu erro! " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
 
	@PutMapping("/enable/{id}")
	public ResponseEntity<String> enable(@PathVariable Long id) {
		try {
			String msn = this.usuarioService.enable(id);
			return new ResponseEntity<>(msn, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Deu erro! " + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	

}
