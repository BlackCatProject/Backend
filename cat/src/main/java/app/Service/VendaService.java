package app.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.Entity.Produto;
import app.Entity.ProdutoVenda;
import app.Entity.Usuario;
import app.Entity.Venda;
import app.Repository.UsuarioRepository;
import app.Repository.VendaRepository;

@Service
public class VendaService {
	@Autowired
	private VendaRepository vendaRepository;

	@Autowired
	private ProdutoService produtoService;
	@Autowired
	private UsuarioRepository usuarioRepository;

	public String save(Venda venda) {
		validarVenda(venda);

		venda = registrarVenda(venda);

		// toda vez que tiver um relacionamento @onetomany e que vc salva em cascata,
		// precisa fazer isso pra não ficar nula a chave estrangeira da venda no produto
		// venda
		if (venda.getProdutosVenda() != null) {
			for (int i = 0; i < venda.getProdutosVenda().size(); i++) {
				venda.getProdutosVenda().get(i).setVenda(venda);
			}
		}

		this.vendaRepository.save(venda);
		return "Venda salva com sucesso";
	}
	
	private void validarVenda(Venda venda) {
	    // Verificar se o usuário está ativo
	    if (venda.getUsuario() == null || !venda.getUsuario().isAtivo()) {
	        throw new RuntimeException("Erro: " + venda.getUsuario().getNome() + " foi desativado");
	    }

	    // Verificar se a lista de produtos não está vazia
	    if (venda.getProdutosVenda() == null || venda.getProdutosVenda().isEmpty()) {
	        throw new RuntimeException("A lista de produtos não pode estar vazia");
	    }

	    // Verificar se todos os produtos estão ativos
	    for (ProdutoVenda produtoVenda : venda.getProdutosVenda()) {
	        Produto produto = produtoService.findById(produtoVenda.getProduto().getId());
	        if (!produto.isAtivo()) {
	            throw new RuntimeException("Produto inativo, venda não permitida");
	        }
	        produtoVenda.setProduto(produto); // Atualizar o ProdutoVenda com o produto encontrado
	    }

	    // Verificar se a data da venda não está no futuro
	    if (venda.getData() == null || venda.getData().isAfter(LocalDateTime.now())) {
	        throw new RuntimeException("Venda com data no futuro não permitida");
	    }
	    
	    if (venda.getProdutosVenda() != null) {
	        for (ProdutoVenda produtoVenda : venda.getProdutosVenda()) {
	            if (produtoVenda.getQuantidade() <= 0) {
	                throw new RuntimeException("Quantidade do produto inválida");
	            }
	            produtoVenda.setVenda(venda);
	        }
	    }
	    List<String> formasPagamentoValidas = Arrays.asList("Cartão de Débito", "Cartão de Crédito", "Dinheiro", "Cheque");
	    if (!formasPagamentoValidas.contains(venda.getFormaPagamento())) {
	        throw new RuntimeException("Forma de pagamento inválida");
	    }
	}



	public String update(Venda venda, long id) {

		venda = this.atualizarVenda(venda, id);
		this.vendaRepository.save(venda);
		return "Atualizada com sucesso";
	}

	public Venda findById(long id) {
		Optional<Venda> optional = this.vendaRepository.findById(id);
		if (optional.isPresent()) {
			return optional.get();
		} else
			return null;
	}

	public List<Venda> findAll() {
		return this.vendaRepository.findAll();
	}

	public String delete(Long id) {
		this.vendaRepository.deleteById(id);
		return "Venda deletada com sucesso";
	}

	private Venda registrarVenda(Venda venda) {

		// Verificar se o usuário está ativo no banco de dados
		Usuario usuario = usuarioRepository.findById(venda.getUsuario().getId())
				.orElseThrow(() -> new RuntimeException("Usuário não encontrado")); // arrumar e nao usar ->

		if (!usuario.isAtivo()) {
			throw new RuntimeException("Erro: " + usuario.getNome() + " foi desativado");
		}

		venda.setProdutosVenda(this.verificarProdutos(venda.getProdutosVenda()));
		double valorTotal = calcularTotal(venda);
		venda.setTotal(valorTotal);
		venda.setNfe(gerarNfe());
		venda.setData(LocalDateTime.now());

		return venda;
	}

	private Venda atualizarVenda(Venda venda, long id) {

		if (!venda.getUsuario().isAtivo()) {
			throw new RuntimeException("Erro: " + venda.getUsuario().getNome() + " foi desativado");
		}

		Venda vendaInDb = findById(id);
		venda.setId(id);
		venda.setProdutosVenda(this.verificarProdutos(venda.getProdutosVenda()));
		venda.setData(vendaInDb.getData());
		venda.setNfe(vendaInDb.getNfe());
		double valorTotal = calcularTotal(venda);
		venda.setTotal(valorTotal);

		return venda;
	}

	private List<ProdutoVenda> verificarProdutos(List<ProdutoVenda> produtosVenda) {
		List<ProdutoVenda> listTemp = new ArrayList<>();

		for (ProdutoVenda produtoVenda : produtosVenda) {
			// Verificar se o produto está ativo no banco de dados
			Produto produto = produtoService.findById(produtoVenda.getProduto().getId());

			// Se o produto não for encontrado, lançar uma exceção
			if (produto == null) {
				throw new RuntimeException("Produto não encontrado");
			}
			// Verificar se o produto está ativo
			if (!produto.isAtivo()) {
				throw new RuntimeException("Erro: o produto " + produto.getNome() + " foi desativado.");
			}

			boolean encontrou = false;
			for (ProdutoVenda tempProdutoVenda : listTemp) {
				if (produto.getId() == tempProdutoVenda.getProduto().getId()) {
					encontrou = true;
					tempProdutoVenda.setQuantidade(tempProdutoVenda.getQuantidade() + produtoVenda.getQuantidade());
				}
			}

			if (!encontrou) {
				produtoVenda.setProduto(produto); // Garantir que o produto atualizado está no ProdutoVenda
				listTemp.add(produtoVenda);
			}
		}
		return listTemp;
	}

	private double calcularTotal(Venda venda) {

		double valorTotal = 0;

		for (ProdutoVenda p : venda.getProdutosVenda()) {
			if (p.getProduto().getId() == 0) {
				this.produtoService.save(p.getProduto());
				valorTotal += p.getProduto().getPreco() * p.getQuantidade();
			} else {
				Produto produto = this.produtoService.findById(p.getProduto().getId());
				valorTotal += produto.getPreco() * p.getQuantidade();
			}
		}

		if (venda.getDesconto() == 0) {
			return valorTotal;
		} else {
			return valorTotal * (1 - (venda.getDesconto() / 100.0));
		}
	}

	private long gerarNfe() {

		Random random = new Random();
		long numNfe;
		boolean exists = false;
		do {
			numNfe = random.nextInt(900000000) + 100000000;
			exists = vendaRepository.existsByNfe(numNfe);
		} while (exists);

		return numNfe;
	}

	public List<Venda> findByData(LocalDateTime startDate, LocalDateTime endDate) {
		return this.vendaRepository.findByDataBetween(startDate, endDate);
	}

	public List<Venda> findByMonthAndYear(int mes, int ano) {
		// Definindo o primeiro e o último dia do mês
		YearMonth yearMonth = YearMonth.of(ano, mes);
		LocalDateTime startDate = yearMonth.atDay(1).atStartOfDay();
		LocalDateTime endDate = yearMonth.atEndOfMonth().atTime(23, 59, 59);

		return vendaRepository.findByDataBetween(startDate, endDate);
	}

	public List<Venda> findByUsuarioId(long usuarioId) {
		return vendaRepository.findByUsuarioId(usuarioId);
	}

	public Venda buscarPorNumeroNfe(long numeroNfe) {
		return vendaRepository.findByNfe(numeroNfe).orElseThrow(
				() -> new NoSuchElementException("Venda não encontrada com o número da NFe: " + numeroNfe));
	}

}
