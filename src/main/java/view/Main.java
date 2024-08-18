package view;

import controller.FormaPagamentoController;
import controller.ProdutoController;
import controller.UsuarioController;
import controller.VendaController;
import model.Entity.*;
import model.Repository.ProdutoRepository;
import model.Repository.UsuarioRepository;
import model.Repository.VendaRepository;
import model.Service.ProdutoService;
import model.Service.UsuarioService;
import model.Service.VendaService;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Main {
    private static Scanner sc = new Scanner(System.in);
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("BancoPie");
    private static EntityManager em = emf.createEntityManager();

    private static ProdutoRepository produtoRepository = new ProdutoRepository(em);
    private static ProdutoService produtoService = new ProdutoService(produtoRepository);
    private static ProdutoController produtoController = new ProdutoController(produtoService);

    private static UsuarioRepository usuarioRepository = new UsuarioRepository(em);
    private static UsuarioService usuarioService = new UsuarioService(usuarioRepository);
    private static UsuarioController usuarioController = new UsuarioController(usuarioService);

    private static VendaRepository vendaRepository = new VendaRepository(em);
    private static VendaService vendaService = new VendaService(vendaRepository);
    private static VendaController vendaController = new VendaController(vendaService);
    private static Usuario usuarioLogado;

    private static FormaPagamentoController formaPagController = new FormaPagamentoController(em);


    public static void main(String[] args) {

        Login();
    }
//=====================================================================================================================

    public static void Login(){
        System.out.println("----------> Realize o login <--------------");

        System.out.println("Nome de usuario:");
        String nome = sc.nextLine();

        System.out.println("Senha:");
        String senha = sc.nextLine();

        usuarioLogado = usuarioController.login(nome, senha);
        //UsuarioController usuarioController = new UsuarioController();

        if (usuarioLogado != null) {
            System.out.println("Bem-Vind@, " + usuarioLogado.getNome() + "!");
            if (usuarioLogado.isUserRole()) {
                menuAdmin();
            } else {
                menuFuncionario();
            }
        } else {
            System.out.println("Nome de usuário ou senha incorretos. Por favor, tente novamente.");
            Login();
        }


    }


    //==================================================================================
    public static void menuFuncionario() {
        boolean sair = false;

        while (!sair) {
            System.out.println("===== Menu Funcionário =====");
            System.out.println("-> 1. Ver produtos");
            System.out.println("-> 2. Realizar venda");
            System.out.println("-> 3. Ver vendas");
            System.out.println("-> 0. Sair");
            System.out.print("Escolha uma opção: ");

            int opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1:
                    listarProdutos();
                    aguardarEnter();
                    menuFuncionario();
                    break;
                case 2:
                    RealizarVenda();
                    aguardarEnter();
                    menuFuncionario();
                    break;
                case 3:
                    ConsultarVendas();
                    aguardarEnter();
                    menuFuncionario();
                    break;
                case 0:
                    sair = true;
                    System.out.println("Volte sempre.");
                    Login();
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    //=====================================================================================================================
    public static void menuAdmin() {
        Scanner sc = new Scanner(System.in);

        boolean sair = false;

        while(!sair) {

            System.out.println("===== Menu Principal =====");
            System.out.println("-> 1. Gerenciar usuarios");
            System.out.println("-> 2. Gerenciar produtos");
            System.out.println("-> 3. Gerenciar vendas");
            System.out.println("-> 0. sair \n");
            System.out.print("Escolha uma opção: ");

            int opcao = sc.nextInt();
            sc.nextLine();

            switch(opcao) {
                case 1:
                    gerenciarUser();
                    aguardarEnter();
                    break;
                case 2:
                    gerenciarProdutos();
                    aguardarEnter();
                    break;
                case 3:
                    GerenciarVendas();
                    break;
                case 4:

                    break;
                case 0:
                    sair = true;
                    System.out.println("Volte sempre..........");
                    Login();
                    break;

                default:
                    System.out.println("Opcao invalida. Tente novamente");

            }

        }

    }
//=====================================================================================================================
    private static void gerenciarUser() {

        System.out.println("=======- Gestao de funcionarios -=======");
        System.out.println("1 - Novo usuario");
        System.out.println("2 - Lista de usuario");
        System.out.println("3 - Excluir usuario");
        System.out.println("4 - Editar usuario");
        System.out.println("0 - Voltar ao menu principal");
        System.out.println("Escolha uma opção:");

        int opcao = sc.nextInt();
        sc.nextLine();

        switch (opcao) {
            case 1:
                criaUsuario();
                aguardarEnter();
                gerenciarUser();
                break;
            case 2:
                listarUsuario();
                aguardarEnter();
                gerenciarUser();

                break;
            case 3:
                System.out.println("Digite o ID do funcionário a ser excluído:");
                long deleteId = sc.nextLong();
                sc.nextLine();
                usuarioController.deleteUserById(deleteId);
                System.out.println("Funcionário excluído.");
                aguardarEnter();
                gerenciarUser();
                break;
            case 4:
                editarUsuario();
                aguardarEnter();
                gerenciarUser();
                break;
            case 0:
                menuAdmin();
                break;
            default:
                System.out.println("Opção inválida.");
                gerenciarUser();
                break;
        }
    }

    public static void criaUsuario(){


        Usuario novoUser = new Usuario();

        System.out.println("Digite o nome do novo usuario:");
        String nome = sc.nextLine();
        novoUser.setNome(nome);

        System.out.println("Digite a senha do novo usuario:");
        String senha = sc.nextLine();

        Usuario usuarioExistente = usuarioRepository.findBysenha(senha);
        if ((usuarioExistente != null)){
            System.out.println("Já existe um funcionário com a mesma senha. Por favor, escolha outra senha.");
            return;
        }
        novoUser.setSenha(senha);

        System.out.println("Digite o tipo de usuario usuario: (True sendo adiministrador e false sendo funcionarios)");
        boolean userRole = sc.nextBoolean();
        novoUser.setUserRole(userRole);

        Usuario criandoUsuario = usuarioController.createUser(novoUser);

        if(criandoUsuario != null) {
            System.out.println("Novo usuario criado com sucesso.");
            System.out.println("ID: " + criandoUsuario.getId());
            System.out.println("Nome: " + criandoUsuario.getNome());
            System.out.println("Senha: " + criandoUsuario.getSenha());
            System.out.println("Cargo: " + criandoUsuario.getUserRole());
        }else {
            System.out.println("Falha ao criar novo usuario.");
        }
    }

    public static void listarUsuario(){

        List<Usuario> usuarios = usuarioRepository.findAll();
        if(usuarios.isEmpty()){
            System.out.println("Não há usuarios cadastrados no sistema.");
        }else {
            System.out.println("Lista de usuarios cadastrados");
            for (Usuario usuario : usuarios){
                System.out.println("Id:"+usuario.getId()+ ", Nome: "+usuario.getNome());
            }
        }
    }

    public static void editarUsuario(){
        System.out.println("Digite o ID do funcionário a ser editado:");
        long id = sc.nextLong();
        sc.nextLine();

        Usuario usuario = usuarioService.findUserById(id);
        if (usuario != null) {
            System.out.println("Digite o novo nome do usuário (ou pressione Enter para manter o atual: " + usuario.getNome() + "):");
            String novoNome = sc.nextLine();
            if (!novoNome.isEmpty()) {
                usuario.setNome(novoNome);
            }

            System.out.println("Digite a nova senha do usuário (ou pressione Enter para manter o atual: " + usuario.getSenha() + "):");
            String novaSenha = sc.nextLine();
            if (!novaSenha.isEmpty() && !novaSenha.equals(usuario.getSenha())) {
                usuario.setSenha(novaSenha);
            } else if (novaSenha.isEmpty()) {
                System.out.println("A senha não pode ficar em branco. A senha não foi alterada.");
            } else {
                System.out.println("A senha não pode ser a mesma que a atual. A senha não foi alterada.");
            }

            System.out.println("Digite o novo tipo de usuário (ou pressione Enter para manter o atual: " + usuario.getUserRole() + "):");
            String novoUserRole = sc.nextLine();
            if (!novoUserRole.isEmpty()) {
              
                boolean novoUserRoleBoolean = Boolean.parseBoolean(novoUserRole);
                usuario.setUserRole(novoUserRoleBoolean);
            }

            usuarioController.updateUser(usuario);
            System.out.println("Funcionário atualizado com sucesso!");
        } else {
            System.out.println("Funcionário com o ID fornecido não encontrado.");
            aguardarEnter();
            gerenciarUser();
        }


    }

//====================================================================================================================
private static void gerenciarProdutos() {

    System.out.println("=======- Gestao de produtos -=======");
    System.out.println("1 - Novo produto");
    System.out.println("2 - Lista de produtos");
    System.out.println("3 - Excluir produto");
    System.out.println("4 - Editar produto");
    System.out.println("0 - Voltar ao menu principal");
    System.out.println("Escolha uma opção:");

    int opcao = sc.nextInt();
    sc.nextLine();

    switch (opcao) {
        case 1:
            criarProduto();
            aguardarEnter();
            gerenciarProdutos();
            break;
        case 2:
            listarProdutos();
            aguardarEnter();
            gerenciarProdutos();

            break;
        case 3:
            System.out.println("Digite o ID do produto a ser excluído:");
            long deleteId = sc.nextLong();
            sc.nextLine();
            produtoController.deleteProdrById(deleteId);
            System.out.println("Produtoio excluído.");
            aguardarEnter();
            gerenciarProdutos();
            break;
        case 4:
            editarProduto();
            aguardarEnter();
            gerenciarProdutos();
            break;
        case 0:
            menuAdmin();
            break;
        default:
            System.out.println("Opção inválida.");
            gerenciarProdutos();
            break;
    }
}

    public static void criarProduto(){
        Produtos novoProduto = new Produtos();

        System.out.println("Digite o nome do novo produto:");
        String nome = sc.nextLine();
        novoProduto.setNome(nome);

        System.out.println("Digite o descrição do novo produto:");
        String descricao = sc.nextLine();
        novoProduto.setDescricao(descricao);

        System.out.println("Digite o preco do novo produto:");
        float preco = sc.nextFloat();
        novoProduto.setPreco(preco);

        Produtos criandoProduto = produtoController.createProduto(novoProduto);

        if(criandoProduto != null) {
            System.out.println("Novo usuario criado com sucesso.");
            System.out.println("ID: " + criandoProduto.getId());
            System.out.println("Nome: " + criandoProduto.getNome());
            System.out.println("Descricao: " + criandoProduto.getDescricao());
            System.out.println("Preco: " + criandoProduto.getPreco());
        }else {
            System.out.println("Falha ao criar novo Produto.");
        }
    }

    public static void listarProdutos(){

        List<Produtos> produtos = produtoRepository.findAll();
        if(produtos.isEmpty()){
            System.out.println("Não há produtos cadastrados no sistema.");
        }else {
            System.out.println("Lista de produtos cadastrados:");
            for (Produtos produto : produtos){
                System.out.println("Id:"+produto.getId()+ ", Nome: "+produto.getNome()+ ", Descricao: "+produto.getDescricao()+", Preco:"+produto.getPreco());
            }

        }
    }

    public static void editarProduto(){
        System.out.println("Digite o ID do produto a ser editado:");
        long id = sc.nextLong();
        sc.nextLine();

        Produtos produtos = produtoService.findProdutoById(id);

        if (produtos != null){
            System.out.println("Digite o novo nome do produto  (ou pressione Enter para manter o atual: "+ produtos.getNome()+"):");
            String novoNome = sc.nextLine();
            if (!novoNome.isEmpty()) {
                produtos.setNome(novoNome);
            }

            System.out.println("Digite a nova descricao do produto (ou pressione Enter para manter o atual: "+ produtos.getDescricao()+"):");
            String novadescriacao = sc.nextLine();
            if (!novadescriacao.isEmpty()){
                produtos.setDescricao(novadescriacao);
            }
            System.out.println("Digite o novo preco do produto (ou pressione Enter para manter o atual: " + produtos.getPreco() + "):");
            String novoPrecoStr = sc.nextLine();
            if (!novoPrecoStr.isEmpty()) {
                try {
                    // Substitui vírgula por ponto antes de converter para float
                    float novoPreco = Float.parseFloat(novoPrecoStr.replace(",", "."));
                    produtos.setPreco(novoPreco);
                } catch (NumberFormatException e) {
                    System.out.println("Preço inválido. Mantendo o preço atual.");
                }
            }

            produtoController.updateProduto(produtos);
            System.out.println("Produtoio atualizado com sucesso!");
        }else {
            System.out.println("Produto com o ID fornecido não encontrado.");
        }
        aguardarEnter();
        gerenciarProdutos();
    }

    //============================================================================================================
    public static void GerenciarVendas(){
        System.out.println("=======- Gestao de Vendas -=======");
        System.out.println("1 - Realizar venda");
        System.out.println("2 - Consultar vendas");
        System.out.println("3 - excluir vendas");
        System.out.println("0 - Voltar ao menu principal");
        System.out.println("Escolha uma opção:");

        int opcao = sc.nextInt();
        sc.nextLine();

        switch (opcao) {
            case 1:
                RealizarVenda();
                aguardarEnter();
                GerenciarVendas();
                break;
            case 2:
                ConsultarVendas();
                aguardarEnter();
                GerenciarVendas();

                break;
            case 3:
                ConsultarVendas();
                System.out.println("Digite o ID da venda a ser excluída:");
                long deleteId = sc.nextLong();
                sc.nextLine();
                vendaController.deleteVendaById(deleteId);
                System.out.println("venda excluído.");
                aguardarEnter();
                GerenciarVendas();
                break;
            case 0:
                menuAdmin();
                break;
            default:
                System.out.println("Opção inválida.");
                GerenciarVendas();
                break;
        }
    }

    public static void ConsultarVendas() {
        vendaRepository = new VendaRepository(em);

        List<Vendas> vendas =  vendaRepository.findALl();
        if(vendas.isEmpty()){
            System.out.println("Não há vendas cadastrados no sistema.");
        } else {
            System.out.println("Lista de vendas realizadas:");
            for (Vendas venda : vendas){
                System.out.println("Id:"+venda.getId()+ ", data: "+venda.getDataVenda()+", Preco:"+venda.getTotal());
            }
        }
    }

    private static List<ProdutoHasVenda> RealizarVenda() {
        if (usuarioLogado == null) {
            System.out.println("Usuário não autenticado.");
            return new ArrayList<>();
        }

        List<ProdutoHasVenda> carrinho = new ArrayList<>();
        float totalVenda = 0;

        System.out.println("Iniciando uma nova venda para o usuário: " + usuarioLogado.getNome());

        boolean adicionandoItens = true;
        while (adicionandoItens) {
            System.out.println("Digite o ID do produto:");
            long produtoId = sc.nextLong();
            sc.nextLine();
            System.out.println("Digite a quantidade:");
            int quantidade = sc.nextInt();
            sc.nextLine();

            Produtos produto = produtoController.getProdutoById(produtoId);
            if (produto == null) {
                System.out.println("Produto não encontrado.");
                continue;
            }

            float subtotal = produto.getPreco() * quantidade;

            ProdutoHasVenda itemVenda = new ProdutoHasVenda(quantidade, subtotal, produto, null);
            carrinho.add(itemVenda);

            totalVenda += subtotal;
            System.out.println("Subtotal do item: " + subtotal);
            System.out.println("Total da venda até agora: " + totalVenda);

            System.out.println("Deseja adicionar mais itens ao carrinho? (s/n) ou cancelar a venda (0)");
            String resposta = sc.nextLine();
            if (resposta.equalsIgnoreCase("n")) {
                adicionandoItens = false;
            } else if (resposta.equals("0")) {
                System.out.println("Deseja cancelar a venda? (s/n)");
                resposta = sc.nextLine();
                if (resposta.equalsIgnoreCase("s")) {
                    carrinho.clear();
                    totalVenda = 0;
                    System.out.println("Venda cancelada.");
                    return carrinho; // Retorna o carrinho vazio em caso de cancelamento da venda
                }
            }
        }

        System.out.println("Escolha a forma de pagamento (Digite o ID da forma de pagamento):");
        System.out.println("|  1 | Dinheiro                                           |");
        System.out.println("|  2 | Pix                                                |");
        System.out.println("|  3 | Cartão de Crédito                                  |");
        System.out.println("|  4 | Cartão de Débito                                   |");
        long formaPagamentoId = sc.nextLong();
        sc.nextLine();
        FormaPagamento formaPagamento = formaPagController.findFormaPagamentoById(formaPagamentoId);

        if (formaPagamento == null) {
            System.out.println("Forma de pagamento não encontrada.");
            return carrinho;
        }

        if (formaPagamentoId == 3) { // Se for cartão de crédito
            System.out.println("Quantas parcelas deseja? (1 a 3)");
            int numParcelas = sc.nextInt();
            sc.nextLine();

            if (numParcelas < 1 || numParcelas > 3) {
                System.out.println("Número de parcelas inválido.");
                return carrinho;
            }

            // Adiciona juros de acordo com o número de parcelas
            float juros = 0;
            if (numParcelas == 2) {
                juros = 0.05f; // 5% de juros para 2 parcelas
            } else if (numParcelas == 3) {
                juros = 0.1f; // 10% de juros para 3 parcelas
            }

            totalVenda *= (1 + juros); // Aumenta o total da venda com os juros
        }

        // Confirmar a geração da Nota Fiscal Eletrônica (NFe)
        System.out.println("Deseja gerar a Nota Fiscal Eletrônica (NFe)? (s/n)");
        String gerarNfe = sc.nextLine();
        if (!gerarNfe.equalsIgnoreCase("s")) {
            System.out.println("Venda cancelada.");
            return carrinho; // Retorna o carrinho se o usuário não desejar gerar a NFe
        }

        long nfeValue = generateNfeNumber();

        System.out.println("Concluindo venda...");

        // Criar uma nova instância de venda
        Vendas venda = new Vendas();
        venda.setTotal(totalVenda);
        venda.setFormaPagamento(formaPagamento);
        venda.setNfe(nfeValue);
        venda.setUsuario(usuarioLogado);
        venda.setDataVenda(LocalDateTime.now());

        // Persistir a venda e os itens
        vendaController.saveVenda(venda);
        for (ProdutoHasVenda item : carrinho) {
            item.setVendas(venda);
            vendaController.saveProdutoHasVenda(item);
        }

        // Exibir a Nota Fiscal Eletrônica
        exibirNFe(carrinho, totalVenda, nfeValue);

        System.out.println("Venda concluída com sucesso!");
        System.out.println("Nota Fiscal Eletrônica (NFe) gerada com número: " + nfeValue);

        return carrinho;
    }

    private static void exibirNFe(List<ProdutoHasVenda> carrinho, float totalVenda, long nfeValue) {
        System.out.println("===============================");
        System.out.println("       Nota Fiscal Eletrônica   ");
        System.out.println("===============================");
        for (ProdutoHasVenda item : carrinho) {
            System.out.println("Produto: " + item.getProdutos().getNome());
            System.out.println("Quantidade: " + item.getQuantidade());
            System.out.println("Preço unitário: " + item.getProdutos().getPreco());
            System.out.println("Subtotal: " + item.getSubtotal());
            System.out.println("--------------------------------");
        }
        System.out.println("Total da compra: " + totalVenda);
        System.out.println("Número da NFe: " + nfeValue);
        System.out.println("===============================");
    }


    private static long generateNfeNumber() {

        return System.currentTimeMillis();
    }



    //====================================================================================================================
    private static void aguardarEnter() {
        System.out.println("Pressione Enter para continuar...");
        sc.nextLine();
    }

}
