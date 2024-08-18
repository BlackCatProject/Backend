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
import org.springframework.web.bind.annotation.RestController;

import app.Entity.Produto;
import app.Entity.Venda;
import app.Service.ProdutoService;
import app.Service.VendaService;

@RestController
@RequestMapping("/produto")
public class ProdutoController {
	@Autowired
	private ProdutoService produtoService  ;
	
	
	@PostMapping("/save")
	public ResponseEntity<String> save(@RequestBody Produto produto  ) {
	    try {
	        String msn = this.produtoService.save(produto);
	        return new ResponseEntity<>(msn, HttpStatus.OK);
	    } catch (Exception e) {
	        return new ResponseEntity<>("Deu Erro! " + e.getMessage(), HttpStatus.BAD_REQUEST);
	    }
	}
	
	 @PutMapping("/update/{id}")
	    public ResponseEntity<String> update(@RequestBody Produto produto, @PathVariable Long id) {
	        try {
	            String msn = this.produtoService.update(produto, id);
	            return new ResponseEntity<>(msn, HttpStatus.OK);
	        } catch (Exception e) {
	            return new ResponseEntity<>("Deu erro! " + e.getMessage(), HttpStatus.BAD_REQUEST);
	        }
	    }
	 
	 @GetMapping("/findById/{index}")
	 public ResponseEntity<Produto> findById(@PathVariable int index) {
	     try {
	    	 Produto  funcionario = this.produtoService.findById(index);
	         return new ResponseEntity<>(funcionario, HttpStatus.OK);
	     } catch (Exception e) {
	         return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
	     }
	 }

		@GetMapping("/findAll")
		public ResponseEntity<List<Produto>> findAll() {
			try {
				List<Produto> lista = this.produtoService.findAll();
				return new ResponseEntity<>(lista, HttpStatus.OK);
			} catch (Exception e) {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
		}

		@DeleteMapping("/delete/{id}")
		public ResponseEntity<String> delete(@PathVariable Long id) {
		    try {
		        String msn = this.produtoService.delete(id);
		        return new ResponseEntity<>(msn, HttpStatus.OK);
		    } catch (Exception e) {
		        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		    }
		}
}
