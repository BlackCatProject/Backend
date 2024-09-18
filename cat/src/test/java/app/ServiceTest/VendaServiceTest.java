package app.ServiceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

		// vendat
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
	void testVendaComUsuarioInativo() {
		Usuario usuarioInativo = new Usuario();
		usuarioInativo.setId(1L);
		usuarioInativo.setNome("Maria Pinto Souza");
		usuarioInativo.setAtivo(false);

		Produto produto = new Produto();
		produto.setId(1L);
		produto.setNome("Torta de Banana");
		produto.setPreco(99.99);
		produto.setAtivo(true);

		ProdutoVenda produtoVenda = new ProdutoVenda();
		produtoVenda.setProduto(produto);
		produtoVenda.setQuantidade(1);

		Venda venda = new Venda();
		venda.setUsuario(usuarioInativo);
		venda.setProdutosVenda(Arrays.asList(produtoVenda));
		venda.setDesconto(10);
		venda.setData(LocalDateTime.now());

		Mockito.when(usuarioService.findById(1L)).thenReturn(usuarioInativo);

		RuntimeException thrown = assertThrows(RuntimeException.class, () -> vendaService.save(venda));
		assertEquals("Erro: Maria Pinto Souza foi desativado", thrown.getMessage());
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

		assertEquals("Erro: o produto torta de banana foi desativado.", exception.getMessage());

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
	@DisplayName("Erro ao tentar realizar venda com produto inativo")
	void ProdutoInativo() {

		Usuario usuario = new Usuario();
		usuario.setId(3L);
		usuario.setNome("jose de amado");
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

		assertEquals("Erro: o produto torta de maçã foi desativado.", exception.getMessage());

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
	@DisplayName("Erro ao tentar realizar venda com forma de pagamento inválida")
	void testVendaComFormaPagamentoInvalida() {
		Usuario usuario = new Usuario();
		usuario.setId(3L);
		usuario.setNome("José de Amado");
		usuario.setAtivo(true);

		Produto produto = new Produto();
		produto.setId(1L);
		produto.setNome("Torta de Banana");
		produto.setPreco(99.99);
		produto.setAtivo(true);

		ProdutoVenda produtoVenda = new ProdutoVenda();
		produtoVenda.setProduto(produto);
		produtoVenda.setQuantidade(1);

		Venda venda = new Venda();
		venda.setUsuario(usuario);
		venda.setProdutosVenda(Arrays.asList(produtoVenda));
		venda.setDesconto(10);
		venda.setFormaPagamento("Forma Inválida");
		venda.setData(LocalDateTime.now());

		Mockito.when(produtoService.findById(1L)).thenReturn(produto);
		Mockito.when(usuarioService.findById(3L)).thenReturn(usuario);

		RuntimeException thrown = assertThrows(RuntimeException.class, () -> vendaService.save(venda));
		assertEquals("Forma de pagamento inválida", thrown.getMessage());
	}

	@Test
	@DisplayName("Atualizar venda com sucesso")
	void testUpdateVenda() {

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
		produtoVenda.setQuantidade(2);

		Venda vendaExistente = new Venda();
		vendaExistente.setId(1L);
		vendaExistente.setUsuario(usuario);
		vendaExistente.setProdutosVenda(Arrays.asList(produtoVenda));
		vendaExistente.setDesconto(5);
		vendaExistente.setFormaPagamento("Dinheiro");
		vendaExistente.setData(LocalDateTime.now().minusDays(2));
		vendaExistente.setNfe(123456789L);

		Mockito.when(vendaRepository.findById(1L)).thenReturn(Optional.of(vendaExistente));

		Venda vendaAtualizada = new Venda();
		vendaAtualizada.setUsuario(usuario);
		vendaAtualizada.setProdutosVenda(Arrays.asList(produtoVenda));
		vendaAtualizada.setDesconto(10);
		vendaAtualizada.setFormaPagamento("Cartão de Crédito");
		vendaAtualizada.setData(LocalDateTime.now());

		Mockito.when(vendaRepository.save(Mockito.any(Venda.class))).thenReturn(vendaAtualizada);

		String result = vendaService.update(vendaAtualizada, 1L);

		Mockito.verify(vendaRepository, times(1)).save(Mockito.any(Venda.class));

		assertEquals("Atualizada com sucesso", result);

		assertEquals(vendaExistente.getData(), vendaAtualizada.getData()); // A data da venda existente deve ser mantida
		assertEquals(vendaExistente.getNfe(), vendaAtualizada.getNfe()); // A NFe da venda existente deve ser mantida
	}

	@Test
	@DisplayName("Buscar vendas por data")
	void testFindByData() {

		LocalDateTime startDate = LocalDateTime.of(2023, 9, 1, 0, 0);
		LocalDateTime endDate = LocalDateTime.of(2023, 9, 30, 23, 59);

		List<Venda> vendas = Arrays.asList(new Venda(), new Venda());

		Mockito.when(vendaRepository.findByDataBetween(startDate, endDate)).thenReturn(vendas);

		List<Venda> result = vendaService.findByData(startDate, endDate);

		assertEquals(2, result.size());
		Mockito.verify(vendaRepository, Mockito.times(1)).findByDataBetween(startDate, endDate);
	}

	@Test
	@DisplayName("Erro ao tentar realizar venda com produto inativo")
	void VendaComProdutoInativo() {
		Usuario usuario = new Usuario();
		usuario.setId(3L);
		usuario.setNome("jose de amado");
		usuario.setAtivo(true);

		Produto produtoInativo = new Produto();
		produtoInativo.setId(1L);
		produtoInativo.setNome("Torta de Maçã");
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

		Exception exception = assertThrows(RuntimeException.class, () -> vendaService.save(venda));
		assertEquals("Erro: o produto Torta de Maçã foi desativado.", exception.getMessage());
	}

	@Test
	@DisplayName("Buscar vendas por mês e ano")
	void BuscarPorMesEAno() {

		int mes = 9;
		int ano = 2023;
		YearMonth yearMonth = YearMonth.of(ano, mes);
		LocalDateTime startDate = yearMonth.atDay(1).atStartOfDay();
		LocalDateTime endDate = yearMonth.atEndOfMonth().atTime(23, 59, 59);

		List<Venda> vendas = Arrays.asList(new Venda(), new Venda());
		Mockito.when(vendaRepository.findByDataBetween(startDate, endDate)).thenReturn(vendas);

		List<Venda> result = vendaService.findByMonthAndYear(mes, ano);

		assertEquals(2, result.size());
		Mockito.verify(vendaRepository, Mockito.times(1)).findByDataBetween(startDate, endDate);
	}

	@Test
	@DisplayName("Buscar vendas por ID do usuário")
	void BuscarIdDoUsuario() {

		long usuarioId = 1L;
		List<Venda> vendas = Arrays.asList(new Venda(), new Venda());
		Mockito.when(vendaRepository.findByUsuarioId(usuarioId)).thenReturn(vendas);

		List<Venda> result = vendaService.findByUsuarioId(usuarioId);

		assertEquals(2, result.size());
		Mockito.verify(vendaRepository, Mockito.times(1)).findByUsuarioId(usuarioId);
	}

	@Test
	@DisplayName("Buscar venda por número da NFe com sucesso")
	void BuscarVendaPorNfe() {

		long numeroNfe = 123456789L;
		Venda venda = new Venda();
		Mockito.when(vendaRepository.findByNfe(numeroNfe)).thenReturn(Optional.of(venda));

		Venda result = vendaService.buscarPorNumeroNfe(numeroNfe);

		assertNotNull(result);
		Mockito.verify(vendaRepository, Mockito.times(1)).findByNfe(numeroNfe);
	}

	@Test
	@DisplayName("Buscar venda por número da NFe e lançar exceção quando não encontrada")
	void VendaNaoEncontradaPorNfe() {
		long numeroNfe = 123456789L;
		Mockito.when(vendaRepository.findByNfe(numeroNfe)).thenReturn(Optional.empty());

		Exception exception = assertThrows(NoSuchElementException.class, () -> {
			vendaService.buscarPorNumeroNfe(numeroNfe);
		});

		assertEquals("Venda não encontrada com o número da NFe: " + numeroNfe, exception.getMessage());
	}

	@Test
	@DisplayName("Buscar todas as vendas")
	void testFindAll() {

		Usuario usuario = new Usuario();
		usuario.setId(3L);
		usuario.setNome("José de Amado");
		usuario.setAtivo(true);

		Produto produto = new Produto();
		produto.setId(1L);
		produto.setNome("Torta de Banana");
		produto.setPreco(99.99);
		produto.setAtivo(true);

		ProdutoVenda produtoVenda = new ProdutoVenda();
		produtoVenda.setProduto(produto);
		produtoVenda.setQuantidade(1);

		Venda venda1 = new Venda();
		venda1.setId(1L);
		venda1.setUsuario(usuario);
		venda1.setProdutosVenda(Arrays.asList(produtoVenda));
		venda1.setDesconto(10);
		venda1.setFormaPagamento("Cartão de Débito");
		venda1.setData(LocalDateTime.now());

		Venda venda2 = new Venda();
		venda2.setId(2L);
		venda2.setUsuario(usuario);
		venda2.setProdutosVenda(Arrays.asList(produtoVenda));
		venda2.setDesconto(5);
		venda2.setFormaPagamento("Dinheiro");
		venda2.setData(LocalDateTime.now());

		List<Venda> vendas = Arrays.asList(venda1, venda2);

		Mockito.when(vendaRepository.findAll()).thenReturn(vendas);

		List<Venda> result = vendaService.findAll();

		assertNotNull(result);
		assertEquals(2, result.size());
		assertTrue(result.contains(venda1));
		assertTrue(result.contains(venda2));
		Mockito.verify(vendaRepository, Mockito.times(1)).findAll();
	}

	@Test
	@DisplayName("Buscar venda por ID quando não encontrada")
	void FindByNaoEncontrada() {
		long idInexistente = 999L;

		Mockito.when(vendaRepository.findById(idInexistente)).thenReturn(Optional.empty());

		Venda result = vendaService.findById(idInexistente);

		assertNull(result);
		Mockito.verify(vendaRepository, Mockito.times(1)).findById(idInexistente);
	}

	@Test
	@DisplayName("Excluir uma venda pelo ID")
	void testDelete() {
		Long id = 1L;

		String result = vendaService.delete(id);

		assertEquals("Venda deletada com sucesso", result);
		Mockito.verify(vendaRepository, Mockito.times(1)).deleteById(id);
	}

	@Test
	void UsuarioDesativado() {

		Usuario usuarioMock = Mockito.mock(Usuario.class);
		when(usuarioMock.isAtivo()).thenReturn(false);

		Venda vendaMock = Mockito.mock(Venda.class);
		when(vendaMock.getUsuario()).thenReturn(usuarioMock);

		VendaService vendaService = new VendaService();

		RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
			vendaService.atualizarVenda(vendaMock, 1L);
		});

		assertEquals("Erro: João foi desativado", thrown.getMessage());
	}

}