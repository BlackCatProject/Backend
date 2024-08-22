package app.Entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@NotBlank(message = "O nome é obrigatório")
	private String nome;

	@NotBlank(message = "Login é obrigatório")
	@Size(min = 3, max = 20, message = "Login deve ter entre 3 e 20 caracteres")
	private String login;

	@NotBlank(message = "A senha é obrigatória")
	@Size(min = 8, message = "A senha deve ter no minimo 8 caracteres")
	private String senha;

	@NotNull(message = "O tipo de usuaio é obrigatorio")
	@Enumerated(EnumType.STRING)
	private Role role;

	private boolean ativo;

	// setando Role como GESTOR ou FUNCIONARIO
	public enum Role {
		GESTOR, FUNCIONARIO;
	}

	@OneToMany(mappedBy = "usuario")
	@JsonIgnoreProperties("usuario")
	private List<Venda> vendas;

}
