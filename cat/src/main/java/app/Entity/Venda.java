package app.Entity;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Venda {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private double total;
	
	private LocalDateTime data;
	
	private long nfe;
	
	private String formaPagamento;
	
	@ManyToOne
	@JsonIgnoreProperties("venda")
	private Usuario usuario;
	
	@OneToMany(mappedBy = "venda")
	@JsonIgnoreProperties("venda")
	private List<ProdutoVenda> produtosVenda;

}
