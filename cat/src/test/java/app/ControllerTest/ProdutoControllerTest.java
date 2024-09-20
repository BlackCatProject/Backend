package app.ControllerTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import app.Controller.ProdutoController;
import app.Entity.Produto;
import app.Repository.ProdutoRepository;

@SpringBootTest
public class ProdutoControllerTest {

	@Autowired
	private ProdutoController produtoController;

	@MockBean
	ProdutoRepository produtoRepository;

	@BeforeEach
	void setup() {
		Produto produto = new Produto();
		produto.setId(1L);
		produto.setNome("Batata crua");
		produto.setDescricao("Batata crua com casca colhida na esquina da macumba");
		produto.setPreco(99.99);
		produto.setAtivo(true);

		// Simula busca de um produto ja existente com id 1
		Mockito.when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

		// Simula um delete de produto por id
		doNothing().when(produtoRepository).deleteById(anyLong());

		// Simula a existencia de um produto com mesmo nome e descrição
		Mockito.when(produtoRepository.findByNomeAndDescricao(produto.getNome(), produto.getDescricao()))
				.thenReturn(Optional.of(produto));
	}

	@Test
	@DisplayName("Teste ao salvar produto com nome e descrição ja existentes")
	void testSalvarProdutoComNomeEDescricaoExistentes() {
		Produto novoProduto = new Produto();
		novoProduto.setNome("Batata crua");
		novoProduto.setDescricao("Batata crua com casca colhida na esquina da macumba");
		novoProduto.setPreco(100);
		novoProduto.setAtivo(true);
		
		ResponseEntity<String> retorno = produtoController.save(novoProduto);
		
		assertEquals(HttpStatus.BAD_REQUEST, retorno.getStatusCode());
		assertEquals("Erro: Produto com o mesmo nome e descrição já existe", retorno.getBody());
		
	}
	
	@Test
	@DisplayName("Teste ao salvar produto com nome e descrição ja existentes")
	void testSalvarProdutoComPrecoInvalido() {
		Produto novoProduto = new Produto();
		novoProduto.setNome("Batata crua");
		novoProduto.setDescricao("Batata crua com casca colhida na esquina da macumba");
		novoProduto.setPreco(-100);
		novoProduto.setAtivo(true);
		
		
		assertThrows(Exception.class, ()->{
			produtoController.save(novoProduto);
		});
		
	}
	
	


}