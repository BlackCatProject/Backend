package app.Entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
<<<<<<< HEAD
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
=======
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
>>>>>>> be5055aadc76db2eb333b7f56faa928fdf76e48f
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Produto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
<<<<<<< HEAD
	 @NotEmpty(message = "O nome do produto é obrigatório")
=======
	@NotBlank(message = "O nome é obrigatório")
>>>>>>> be5055aadc76db2eb333b7f56faa928fdf76e48f
	private String nome;
	
	private String descricao;
	
<<<<<<< HEAD
	@NotNull(message = "O preço do produto é obrigatório")
	@Positive(message = "O preço deve ser um valor positivo")
	private double preco;
=======
	@NotNull(message = "O preço é obrigatório")
    @DecimalMin(value = "0.0", inclusive = false, message = "O preço deve ser maior que zero")
    private double preco;
>>>>>>> be5055aadc76db2eb333b7f56faa928fdf76e48f
	
	@OneToMany(mappedBy = "produto")
	@JsonIgnoreProperties("produto")
	private List<ProdutoVenda> produtosVenda;
}
