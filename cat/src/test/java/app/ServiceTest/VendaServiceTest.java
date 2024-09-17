package app.ServiceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;

import com.mysql.cj.x.protobuf.MysqlxDatatypes.Array;

import app.Entity.Produto;
import app.Entity.ProdutoVenda;
import app.Entity.Usuario;
import app.Entity.Venda;
import app.Repository.VendaRepository;
import app.Service.ProdutoService;
import app.Service.UsuarioService;
import app.Service.VendaService;

@SpringBootTest
public class VendaServiceTest {

	@Autowired
	VendaService vendaService;

	@MockBean
	VendaRepository vendaRepository;

	@MockBean
	ProdutoService produtoService;

	@MockBean
	UsuarioService usuarioService;

	@BeforeEach
	void setup() {
		// usuarios
		Usuario usuario01 = new Usuario();
		usuario01.setId(3L);
		usuario01.setNome("jose de amado");
		usuario01.setLogin("jose123");
		usuario01.setSenha("senha123");
		usuario01.setRole(Usuario.Role.GESTOR);
		usuario01.setAtivo(true);

		// produto
		Produto produto01 = new Produto();
		produto01.setId(1L);
		produto01.setNome("torta de banana");
		produto01.setDescricao("é uma torta e é de banana");
		produto01.setPreco(99.99);
		produto01.setAtivo(true);

		// venda
		Venda venda01 = new Venda();
		venda01.setId(1L);
		venda01.setData(LocalDateTime.now());
		venda01.setDesconto(10);
		venda01.setFormaPagamento("Cartao de Debito");
		venda01.setUsuario(usuario01);
		venda01.setProdutosVenda(Arrays.asList());
		venda01.setTotal(89.99);

		Mockito.when(vendaRepository.save(Mockito.any())).thenReturn(venda01);

		Mockito.when(produtoService.findById(1L)).thenReturn(produto01);

		Mockito.when(usuarioService.findById(3L)).thenReturn(usuario01);

	}

	@Test
	@DisplayName("Erro ao tentar realizar venda com usuário inativo")
	void VendaUserNegativo() {

		Usuario usuario = new Usuario();
		usuario.setId(1L);
		usuario.setNome("Maria Pinto souza");
		usuario.setAtivo(false);

		Produto produto = new Produto();
		produto.setId(1L);
		produto.setNome("torta de banana");
		produto.setPreco(99.99);
		produto.setAtivo(true);

		ProdutoVenda produtoVenda = new ProdutoVenda();
		produtoVenda.setProduto(produto);
		produtoVenda.setQuantidade(1);

		Venda venda = new Venda();
		venda.setUsuario(usuario);
		venda.setProdutosVenda(Arrays.asList(produtoVenda));
		venda.setDesconto(10);
		venda.setData(LocalDateTime.now());

		Mockito.when(usuarioService.findById(1L)).thenReturn(usuario);

		// Realizar o teste
		Exception exception = assertThrows(RuntimeException.class, () -> {
			vendaService.save(venda);
		});

		assertEquals("Erro: Maria Pinto souza foi desativado", exception.getMessage());
	}

	@Test
	@DisplayName("Erro ao tentar realizar venda com produto inativo")
	void VendaProdutoNegativo() {

		Usuario usuario = new Usuario();
		usuario.setId(3L);
		usuario.setNome("jose de amado");
		usuario.setAtivo(true);

		Produto produto = new Produto();
		produto.setId(9L);
		produto.setNome("torta de banana");
		produto.setPreco(99.99);
		produto.setAtivo(false);

		ProdutoVenda produtoVenda = new ProdutoVenda();
		produtoVenda.setProduto(produto);
		produtoVenda.setQuantidade(1);

		Venda venda = new Venda();
		venda.setUsuario(usuario);
		venda.setProdutosVenda(Arrays.asList(produtoVenda));
		venda.setDesconto(10);

		Mockito.when(produtoService.findById(9L)).thenReturn(produto);

		Exception exception = assertThrows(RuntimeException.class, () -> {
			vendaService.save(venda);
		});

		assertEquals("Produto inativo, venda não permitida", exception.getMessage());
	}

	@Test
	@DisplayName("Erro ao tentar realizar venda com lista de produtos vazia")
	void VendaProdutosVazios() {

		Usuario usuario = new Usuario();
		usuario.setId(3L);
		usuario.setNome("jose de amado");
		usuario.setAtivo(true);

		Produto produto = new Produto();
		produto.setId(9L);
		produto.setNome("torta de banana");
		produto.setPreco(99.99);
		produto.setAtivo(true);

		ProdutoVenda produtoVenda = new ProdutoVenda();
		produtoVenda.setProduto(produto);
		produtoVenda.setQuantidade(1);

		Venda venda = new Venda();
		venda.setUsuario(usuario);
		venda.setProdutosVenda(Arrays.asList());
		venda.setDesconto(10);

		Mockito.when(produtoService.findById(9L)).thenReturn(produto);

		Exception exception = assertThrows(RuntimeException.class, () -> {
			vendaService.save(venda);
		});

		assertEquals("A lista de produtos não pode estar vazia", exception.getMessage());
	}

	@Test
	@DisplayName("Erro ao tentar realizar venda com data no futuro")
	void VendaComDataFutura() {
		Usuario usuario = new Usuario();
		usuario.setId(3L);
		usuario.setNome("jose de amado");
		usuario.setAtivo(true);

		Produto produto = new Produto();
		produto.setId(1L);
		produto.setNome("torta de banana");
		produto.setPreco(99.99);
		produto.setAtivo(true);

		ProdutoVenda produtoVenda = new ProdutoVenda();
		produtoVenda.setProduto(produto);
		produtoVenda.setQuantidade(1);

		Venda venda = new Venda();
		venda.setUsuario(usuario);
		venda.setProdutosVenda(Arrays.asList(produtoVenda));
		venda.setData(LocalDateTime.now().plusDays(1));

		Exception exception = assertThrows(RuntimeException.class, () -> {
			vendaService.save(venda);
		});

		assertEquals("Venda com data no futuro não permitida", exception.getMessage());

	}

	@Test
	@DisplayName("Erro ao tentar realizar venda com produto inativo")
	void ProdutoInativo() {

		Usuario usuario = new Usuario();
		usuario.setId(1L);
		usuario.setNome("João Silva");
		usuario.setAtivo(true);

		Produto produtoInativo = new Produto();
		produtoInativo.setId(1L);
		produtoInativo.setNome("torta de maçã");
		produtoInativo.setPreco(59.99);
		produtoInativo.setAtivo(false);
		ProdutoVenda produtoVenda = new ProdutoVenda();
		produtoVenda.setProduto(produtoInativo);
		produtoVenda.setQuantidade(1);

		Venda venda = new Venda();
		venda.setUsuario(usuario);
		venda.setProdutosVenda(Arrays.asList(produtoVenda));
		venda.setDesconto(10);
		venda.setData(LocalDateTime.now());

		Mockito.when(produtoService.findById(1L)).thenReturn(produtoInativo);

		Exception exception = assertThrows(RuntimeException.class, () -> {
			vendaService.save(venda);
		});

		assertEquals("Produto inativo, venda não permitida", exception.getMessage());
	}

	@Test
	@DisplayName("Salvar venda corretamente ")
	void salvarVenda() {

		Usuario usuario = new Usuario();
		usuario.setId(3L);
		usuario.setNome("João Silva");
		usuario.setAtivo(true);

		Produto produto = new Produto();
		produto.setId(1L);
		produto.setNome("torta de maçã");
		produto.setPreco(59.99);

		ProdutoVenda produtoVenda = new ProdutoVenda();
		produtoVenda.setProduto(produto);
		produtoVenda.setQuantidade(1);

		Venda venda = new Venda();
		venda.setUsuario(usuario);
		venda.setProdutosVenda(Arrays.asList(produtoVenda));
		venda.setDesconto(10);
		venda.setFormaPagamento("Cartão de Débito");
		venda.setData(LocalDateTime.now());

		String result = vendaService.save(venda);

		Mockito.verify(vendaRepository, times(1)).save(venda);
		assertEquals("Venda salva com sucesso", result);

	}

	@Test
	@DisplayName("Erro ao tentar realizar venda com quantidade de produto inválida")
	void QuantInvalida() {
		Usuario usuario = new Usuario();
		usuario.setId(3L);
		usuario.setNome("jose de amado");
		usuario.setAtivo(true);

		Produto produto = new Produto();
		produto.setId(1L);
		produto.setNome("torta de banana");
		produto.setPreco(99.99);
		produto.setAtivo(true);

		ProdutoVenda produtoVenda = new ProdutoVenda();
		produtoVenda.setProduto(produto);
		produtoVenda.setQuantidade(0);

		Venda venda = new Venda();
		venda.setUsuario(usuario);
		venda.setProdutosVenda(Arrays.asList(produtoVenda));
		venda.setDesconto(10);
		venda.setData(LocalDateTime.now());

		Mockito.when(produtoService.findById(1L)).thenReturn(produto);
		Mockito.when(usuarioService.findById(3L)).thenReturn(usuario);

		Exception exception = assertThrows(RuntimeException.class, () -> {
			vendaService.save(venda);
		});

		assertEquals("Quantidade do produto inválida", exception.getMessage());
	}

	@Test
	@DisplayName("Erro ao tentar realizar venda com forma de pagamento inválida")
	void FormaPgInvalida() {
		Usuario usuario = new Usuario();
		usuario.setId(3L);
		usuario.setNome("jose de amado");
		usuario.setAtivo(true);

		Produto produto = new Produto();
		produto.setId(1L);
		produto.setNome("torta de banana");
		produto.setPreco(99.99);
		produto.setAtivo(true);

		ProdutoVenda produtoVenda = new ProdutoVenda();
		produtoVenda.setProduto(produto);
		produtoVenda.setQuantidade(1);

		Venda venda = new Venda();
		venda.setUsuario(usuario);
		venda.setProdutosVenda(Arrays.asList(produtoVenda));
		venda.setDesconto(10);
		venda.setFormaPagamento("Forma Inválida"); // Forma inválida
		venda.setData(LocalDateTime.now());

		Mockito.when(produtoService.findById(1L)).thenReturn(produto);
		Mockito.when(usuarioService.findById(3L)).thenReturn(usuario);

		Exception exception = assertThrows(RuntimeException.class, () -> {
			vendaService.save(venda);
		});

		assertEquals("Forma de pagamento inválida", exception.getMessage());
	}

}