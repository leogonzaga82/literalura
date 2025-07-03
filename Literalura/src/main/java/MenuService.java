import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Scanner;

public class MenuService {

    private final Scanner scanner = new Scanner(System.in);

    public void exibirMenu() {
        int opcao = -1;

        while (opcao != 0) {
            System.out.println("\n=== LITERALURA - Menu ===");
            System.out.println("1 - Buscar livros por autor");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine());

                switch (opcao) {
                    case 1:
                        buscarLivrosPorAutor();
                        break;
                    case 0:
                        System.out.println("Saindo da aplicação.");
                        break;
                    default:
                        System.out.println("Opção inválida.");
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }

    private void buscarLivrosPorAutor() {
        System.out.print("Digite o nome do autor: ");
        String nomeAutor = scanner.nextLine();

        String url = "https://gutendex.com/books/?search=" + nomeAutor.replace(" ", "%20");

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String json = response.body();

                ObjectMapper mapper = new ObjectMapper();
                RespostaApi resposta = mapper.readValue(json, RespostaApi.class);
                List<Livro> livros = resposta.getResults();

                if (livros.isEmpty()) {
                    System.out.println("Nenhum livro encontrado para o autor: " + nomeAutor);
                } else {
                    System.out.println("\n📚 LIVROS ENCONTRADOS:");
                    for (Livro livro : livros) {
                        System.out.println(livro);
                        System.out.println("----------------------------------");
                    }
                }
            } else {
                System.out.println("Erro: status HTTP " + response.statusCode());
            }

        } catch (IOException | InterruptedException e) {
            System.out.println("Erro ao fazer requisição: " + e.getMessage());
        }
    }
}
