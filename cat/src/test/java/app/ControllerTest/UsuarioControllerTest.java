package app.ControllerTest;


import app.Controller.UsuarioController;
import app.Repository.UsuarioRepository;
import app.Service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest

public class UsuarioControllerTest {

    @Autowired
    UsuarioController usuarioController;

    @MockBean
    UsuarioRepository usuarioRepository;

    @MockBean
    UsuarioService usuarioService;

     // sera que coloco aqui @BeforeEach


}
