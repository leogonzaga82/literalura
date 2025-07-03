import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcao = -1;

        while (opcao != 0) {
            System.out.println("\n=== LITERALURA - Menu ===");
            System.out.println("1 - Buscar livros por autor");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");

            try {
                String entrada = scanner.nextLine().trim();
                if (entrada.isBlank()) {
                    System.out.println("Entrada vazia. Tente novamente.");
                    continue;
                }
                opcao = Integer.parseInt(entrada);

                switch (opcao) {
                    case 1:
                        buscarLivrosPorAutor(scanner);
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

    private static void buscarLivrosPorAutor(Scanner scanner) {
        System.out.print("Digite o nome do autor: ");
        String nomeAutor = scanner.nextLine().trim();

        if (nomeAutor.isBlank()) {
            System.out.println("Nome do autor inválido.");
            return;
        }

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

@JsonIgnoreProperties(ignoreUnknown = true)
class RespostaApi {
    private List<Livro> results;

    public List<Livro> getResults() {
        return results;
    }

    public void setResults(List<Livro> results) {
        this.results = results;
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class Livro {
    private String title;
    private List<Autor> authors;
    private List<String> languages;

    @JsonAlias("download_count")
    private int downloadCount;

    public String getTitle() {
        return title;
    }

    public List<Autor> getAuthors() {
        return authors;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public int getDownloadCount() {
        return downloadCount;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthors(List<Autor> authors) {
        this.authors = authors;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public void setDownloadCount(int downloadCount) {
        this.downloadCount = downloadCount;
    }

    @Override
    public String toString() {
        String autor = (authors != null && !authors.isEmpty())
                ? authors.get(0).getName()
                : "Autor desconhecido";

        return "Título: " + title + "\n" +
                "Autor: " + autor + "\n" +
                "Idiomas: " + languages + "\n" +
                "Downloads: " + downloadCount;
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class Autor {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
