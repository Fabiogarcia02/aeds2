import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.DateTimeException;

public class App {

	/** Caminho do arquivo de dados, informado por argumento ou localizado na raiz ou em src. */
    static String nomeArquivoDados;
    
    /** Scanner para leitura de dados do teclado */
    static Scanner teclado;

    /** Vetor de produtos cadastrados */
    static Produto[] produtosCadastrados = new Produto[0];

    /** Quantidade de produtos cadastrados atualmente no vetor */
    static int quantosProdutos = 0;

    static IOrdenator<Produto> ordenador;

    static void limparTela() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /** Gera um efeito de pausa na CLI. Espera por um enter para continuar */
    static void pausa() {
        System.out.println("Digite enter para continuar...");
        if (teclado.hasNextLine()) {
            teclado.nextLine();
        }
    }

    /** Cabeçalho principal da CLI do sistema */
    static void cabecalho() {
        System.out.println("AEDs II COMÉRCIO DE COISINHAS");
        System.out.println("=============================");
    }
    
    static <T extends Number> T lerOpcao(String mensagem, Class<T> classe) {
        
    	T valor;
        
    	System.out.println(mensagem);
        if (!teclado.hasNextLine()) {
            return null;
        }
    	try {
            valor = classe.getConstructor(String.class).newInstance(teclado.nextLine().trim());
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException 
        		| InvocationTargetException | NoSuchMethodException | SecurityException e) {
            return null;
        }
        return valor;
    }
    
    /** Imprime o menu principal, lê a opção do usuário e a retorna (int).
     * Perceba que poderia haver uma melhor modularização com a criação de uma classe Menu.
     * @return Um inteiro com a opção do usuário.
    */
    static int menu() {
        cabecalho();
        System.out.println("1 - Procurar por um produto");
        System.out.println("2 - Ordenar produtos");
        System.out.println("3 - Embaralhar produtos");
        System.out.println("4 - Listar todos os produtos");
        System.out.println("0 - Finalizar");
        
        System.out.println("Digite sua opção: ");
        if (!teclado.hasNextLine()) {
            return 0;
        }
        try {
            return Integer.parseInt(teclado.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    /**
     * Lê os dados de um arquivo-texto e retorna um vetor de produtos. Arquivo-texto no formato
     * N  (quantidade de produtos) <br/>
     * tipo;descrição;preçoDeCusto;margemDeLucro;[dataDeValidade] <br/>
     * Deve haver uma linha para cada um dos produtos. Retorna um vetor vazio em caso de problemas com o arquivo.
     * @param nomeArquivoDados Nome do arquivo de dados a ser aberto.
     * @return Um vetor com os produtos carregados, ou vazio em caso de problemas de leitura.
     */
    static Produto[] lerProdutos(String nomeArquivoDados) {
        quantosProdutos = 0;
        try (Scanner arquivo = new Scanner(new File(nomeArquivoDados), Charset.forName("UTF-8"))) {
            int numProdutos = Integer.parseInt(arquivo.nextLine().trim());
            if (numProdutos < 0) {
                throw new IllegalArgumentException("Quantidade de produtos não pode ser negativa.");
            }
            Produto[] produtos = new Produto[numProdutos];
            for (int i = 0; i < numProdutos; i++) {
                produtos[i] = Produto.criarDoTexto(arquivo.nextLine());
            }
            quantosProdutos = numProdutos;
            return produtos;
        } catch (IOException | IllegalArgumentException | NoSuchElementException
                | IndexOutOfBoundsException | DateTimeException e) {
            System.out.println("Não foi possível carregar os produtos de " + nomeArquivoDados
                    + ": " + e.getMessage());
            return new Produto[0];
        }
    }
    
    static Produto localizarProduto() {
        
    	Produto produto = null;
    	Boolean localizado = false;
    	
    	cabecalho();
    	System.out.println("Localizando um produto...");
        Integer idProduto = lerOpcao("Digite o identificador do produto desejado: ", Integer.class);
        if (idProduto == null) {
            return null;
        }
        for (int i = 0; (i < quantosProdutos && !localizado); i++) {
    		if (produtosCadastrados[i].hashCode() == idProduto) {
        		produto = produtosCadastrados[i];
        		localizado = true;
        	}
        }
        
        return produto;   
    }
    
    private static void mostrarProduto(Produto produto) {
    	
        cabecalho();
        String mensagem = "Produto não encontrado ou identificador inválido!";
        
        if (produto != null){
            mensagem = String.format("Dados do produto:\n%s", produto);
        }
        
        System.out.println(mensagem);
    }
    
    static void ordenarProdutos(){
    	
        cabecalho();
        
        System.out.println("1 - Bubblesort");
        System.out.println("2 - Mergesort");
        System.out.println("3 - Quicksort");
        System.out.println("4 - Seleção");
        System.out.println("5 - Inserção");
        Integer opcao = lerOpcao("Escolha o algoritmo de ordenação: ", Integer.class);
        if (opcao == null) {
            System.out.println("Opção inválida!");
            return;
        }
        IOrdenator<Produto> escolhido = switch (opcao) {
            case 1 -> new Bubblesort<>();
            case 2 -> new Mergesort<>();
            case 3 -> new Quicksort<>();
            case 4 -> new Selecao<>();
            case 5 -> new Insercao<>();
            default -> null;
        };
        if (escolhido == null) {
            System.out.println("Opção inválida!");
            return;
        }
        ordenador = escolhido;

        produtosCadastrados = ordenador.ordenar(produtosCadastrados);        
        System.out.println("Tempo gasto com a ordenação dos produtos: " + ordenador.getTempoOrdenacao() + " ms.");
        System.out.println("Comparações: " + ordenador.getComparacoes());
        System.out.println("Movimentações: " + ordenador.getMovimentacoes());
    }

    static void embaralharProdutos(){
        Collections.shuffle(Arrays.asList(produtosCadastrados));
    }

    /** Lista todos os produtos cadastrados, numerados, um por linha */
    static void listarTodosOsProdutos() {
    	
        cabecalho();
        System.out.println("\nProdutos cadastrados: ");
        for (int i = 0; i < quantosProdutos; i++) {
            System.out.println(String.format("%02d - ID: %d - %s", (i + 1),
                    produtosCadastrados[i].hashCode(), produtosCadastrados[i].toString()));
        }
    }
    
    public static void main(String[] args) {
		teclado = new Scanner(System.in, Charset.forName("UTF-8"));
        nomeArquivoDados = args.length > 0 ? args[0] : "produtos.txt";
        if (args.length == 0 && !new File(nomeArquivoDados).isFile()) {
            nomeArquivoDados = "src/produtos.txt";
        }
        produtosCadastrados = lerProdutos(nomeArquivoDados);
        
        int opcao = -1;
      
        do{
        	opcao = menu();
            switch (opcao) {
                case 1 -> mostrarProduto(localizarProduto());
                case 2 -> ordenarProdutos();
                case 3 -> embaralharProdutos();
                case 4 -> listarTodosOsProdutos();
                case 0 -> System.out.println("FLW VLW OBG VLT SMP.");
                default -> System.out.println("Opção inválida!");
            }
            if (opcao != 0) {
                pausa();
            }
        } while (opcao != 0);       

        teclado.close();    
    }
}
