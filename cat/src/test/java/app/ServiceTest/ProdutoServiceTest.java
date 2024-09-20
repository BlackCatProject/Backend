package app.ServiceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import app.Entity.Produto;
import app.Repository.ProdutoRepository;
import app.Service.ProdutoService;

@SpringBootTest
public class ProdutoServiceTest {

	@Autowired
	public ProdutoService produtoService;

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

		Mockito.when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
		Mockito.when(produtoRepository.findByNomeAndDescricao("Batata crua", "Batata ungida pelo padre Marcelo Rossi"))
				.thenReturn(Optional.of(produto));
		doNothing().when(produtoRepository).delete(Mockito.any());
	}

	/*
	 * @Test
	 * 
	 * @DisplayName("Erro ao usar produto com valor negativo") void
	 * ProdutoValorNegativo() {
	 * 
	 * Produto produto = new Produto(); produto.setId(1L);
	 * produto.setNome("Batata de macumba descascada"); produto.setPreco(-2);
	 * produto.setAtivo(true);
	 * 
	 * Mockito.when(produtoRepository.save(produto)).thenReturn(produto);
	 * 
	 * Exception exception = assertThrows(RuntimeException.class, () -> {
	 * produtoService.save(produto); });
	 * 
	 * assertEquals("Produto com valor negativo, venda não permitida",
	 * exception.getMessage()); }
	 */

	@Test
	@DisplayName("Produto salvo com sucesso!")
	void ProdutoSalvoOk() {

		Produto produto = new Produto();
		produto.setId(1L);
		produto.setNome("Batata de macumba descascada");
		produto.setDescricao("Batata crua com casca colhida na esquina da macumba");
		produto.setPreco(10);
		produto.setAtivo(true);

		Mockito.when(produtoRepository.save(produto)).thenReturn(produto);

		String result = produtoService.save(produto);

		Assertions.assertEquals("Produto salvo com sucesso", result);
	}

	@Test
	@DisplayName("Produto salvo com sucesso!")
	void ProdutoAlteradoOk() {

		Produto produtoExistente = new Produto();
		produtoExistente.setId(1L);
		produtoExistente.setNome("Batata de macumba descascada");
		produtoExistente.setDescricao("Batata crua com casca colhida na esquina da macumba");
		produtoExistente.setPreco(10);
		produtoExistente.setAtivo(true);

		Produto produtoAlterado = new Produto();
		produtoAlterado.setId(1L);
		produtoAlterado.setNome("Batata Santa");
		produtoAlterado.setDescricao("Batata ungida pelo padre Marcelo Rossi");
		produtoAlterado.setPreco(200);
		produtoAlterado.setAtivo(true);

		Mockito.when(produtoRepository.findById(1L)).thenReturn(Optional.of(produtoExistente));
		Mockito.when(produtoRepository.save(produtoAlterado)).thenReturn(produtoAlterado);

		String resultado = produtoService.save(produtoAlterado);

		Mockito.when(produtoRepository.findByNomeAndDescricao("Batata crua", "Batata ungida pelo padre Marcelo Rossi"))
				.thenReturn(Optional.of(produtoAlterado));

		// Mockito.verify(produtoService).save(produtoAlterado);

		Assertions.assertEquals("Produto salvo com sucesso", resultado);
	}

	@Test
	@DisplayName("Produto desativado com sucesso!")
	void ProdutoDesativadoOk() {

		Produto produtoExistente = new Produto();
		produtoExistente.setId(1L);
		produtoExistente.setNome("Batata de macumba descascada");
		produtoExistente.setPreco(10);
		produtoExistente.setAtivo(true);

		// mockando o comportamento no repository
		Mockito.when(produtoRepository.findById(1L)).thenReturn(Optional.of(produtoExistente));

		// executa o metodo desativado
		String resultado = produtoService.disable(1L);

		// verfica se os metodos corretos foram usandos
		Mockito.verify(produtoRepository).findById(1L);
		Mockito.verify(produtoRepository).save(produtoExistente);

		// assertivas
		Assertions.assertEquals("Produto desativado com sucesso!", resultado);
		Assertions.assertFalse((produtoExistente.isAtivo()), "O produto deveria estar desativado.");
	}

}