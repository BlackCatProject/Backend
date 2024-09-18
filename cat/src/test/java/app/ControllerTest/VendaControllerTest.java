package app.ControllerTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import app.Controller.VendaController;
import app.Entity.Produto;
import app.Entity.ProdutoVenda;
import app.Entity.Usuario;
import app.Entity.Usuario.Role;
import app.Entity.Venda;
import app.Repository.VendaRepository;
import app.Service.ProdutoService;
import app.Service.UsuarioService;

@SpringBootTest
public class VendaControllerTest {

	@Autowired
	VendaController vendaController;

	@MockBean
	VendaRepository vendaRepository;
	
	@MockBean
	ProdutoService produtoService;

	@MockBean
	UsuarioService usuarioService;

	@BeforeEach
	void setUp() {

		Usuario usuario = new Usuario(1, "Marcela Garcia", "Marci", "Senha123", Role.FUNCIONARIO, true, null);

		Produto produtoBanana = new Produto(1, "Banana", "Penca de Banana", true, 6, null);

		ProdutoVenda produtoVendaBanana = new ProdutoVenda(1, 1, null, produtoBanana);

		List<ProdutoVenda> list = new ArrayList<>();

		list.add(produtoVendaBanana);

		Venda venda = new Venda(1, 0, null, 0, 0, "Pix", usuario, list);
		
		Mockito.when(vendaRepository.save(Mockito.any())).thenReturn(venda);
		
		Mockito.when(usuarioService.findById(1)).thenReturn(usuario);
		
		Mockito.when(produtoService.findById(1)).thenReturn(produtoBanana);
		
	}

	@Test
	@DisplayName("Integração - Cenário do Save da Venda")
	void cenarioSaveVenda() {

		Usuario usuario = new Usuario(1, "Marcela Garcia", "Marci", "Senha123", Role.FUNCIONARIO, true, null);

		Produto produtoBanana = new Produto(1, "Banana", "Penca de Banana", true, 6, null);

		ProdutoVenda produtoVendaBanana = new ProdutoVenda(1, 1, null, produtoBanana);

		List<ProdutoVenda> list = new ArrayList<>();

		list.add(produtoVendaBanana);

		Venda venda = new Venda(1, 0, null, 0, 0, "Pix", usuario, list);

		ResponseEntity<String> retorno = vendaController.save(venda);

		assertEquals("Venda salva com sucesso", retorno.getBody());
		assertEquals(HttpStatus.OK, retorno.getStatusCode());
	}

}
