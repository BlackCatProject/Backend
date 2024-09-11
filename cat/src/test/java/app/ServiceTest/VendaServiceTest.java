package app.ServiceTest;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import app.Service.ProdutoService;
import app.Service.UsuarioService;
import app.Service.VendaService;

@SpringBootTest
public class VendaServiceTest {
	
	@Autowired
	VendaService vendaService;
	
	@MockBean
	ProdutoService produtoService;
	
	@MockBean
	UsuarioService usuarioService;
	
	@BeforeEach
	void setup() {
		
	}
	

}
