package app.ServiceTest;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
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

		Mockito.when(usuarioService.findById(1L)).thenReturn(usuario01);

	}

	@Test
	public void testSalvarVenda() {
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

		Mockito.when(produtoService.findById(1L)).thenReturn(produto);
		vendaService.save(venda);
		
		double valorEsperado = produto.getPreco() * produtoVenda.getQuantidade() * (1 - (venda.getDesconto() / 100.0));//num sei se ta certo 
		assert (venda.getTotal() == valorEsperado);
		
	}

}