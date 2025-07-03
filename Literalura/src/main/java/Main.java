import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class Main {

    private static final List<Livro> livrosBuscados = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcao = -1;

        while (opcao != 0) {
            System.out.println("\n=== LITERALURA - Menu ===");
            System.out.println("1 - Buscar livros por autor");
            System.out.println("2 - Buscar livros por título");
            System.out.println("3 - Listar todos os livros buscados");
            System.out.println("4 - Listar livros por idioma");
            System.out.println("5 - Listar autores dos livros buscados");
            System.out.println("6 - Listar autores vivos em um determinado ano");
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
                    case 1 -> buscarLivrosPorAutor(scanner);
                    case 2 -> buscarLivrosPorTitulo(scanner);
                    case 3 -> listarTodosOsLivros();
                    case 4 -> listarLivrosPorIdioma(scanner);
                    case 5 -> listarAutores();
                    case 6 -> listarAutoresVivosEmAno(scanner);
                    case 0 -> System.out.println("Saindo da aplicação.");
                    default -> System.out.println("Opção inválida.");
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
        buscarLivros("https://gutendex.com/books/?search=" + nomeAutor.replace(" ", "%20"));
    }

    private static void buscarLivrosPorTitulo(Scanner scanner) {
        System.out.print("Digite o título do livro: ");
        String titulo = scanner.nextLine().trim();
        if (titulo.isBlank()) {
            System.out.println("Título inválido.");
            return;
        }
        buscarLivros("https://gutendex.com/books/?search=" + titulo.replace(" ", "%20"));
    }

    private static void buscarLivros(String url) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                RespostaApi resposta = mapper.readValue(response.body(), RespostaApi.class);
                List<Livro> livros = resposta.getResults();

                if (livros.isEmpty()) {
                    System.out.println("Nenhum livro encontrado.");
                } else {
                    System.out.println("\n📚 LIVROS ENCONTRADOS:");
                    for (Livro livro : livros) {
                        livrosBuscados.add(livro);
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

    private static void listarTodosOsLivros() {
        if (livrosBuscados.isEmpty()) {
            System.out.println("Nenhum livro foi buscado ainda.");
            return;
        }
        System.out.println("\n📚 TODOS OS LIVROS BUSCADOS:");
        for (Livro livro : livrosBuscados) {
            System.out.println(livro);
            System.out.println("----------------------------------");
        }
    }

    private static void listarLivrosPorIdioma(Scanner scanner) {
        System.out.print("Digite o código do idioma (ex: pt, en, fr): ");
        String idioma = scanner.nextLine().trim();

        List<Livro> filtrados = livrosBuscados.stream()
                .filter(l -> !l.getLanguages().isEmpty() && l.getLanguages().get(0).equalsIgnoreCase(idioma))
                .toList();

        if (filtrados.isEmpty()) {
            System.out.println("Nenhum livro encontrado para o idioma: " + idioma);
        } else {
            System.out.println("\n📚 LIVROS NO IDIOMA '" + idioma + "':");
            filtrados.forEach(l -> {
                System.out.println(l);
                System.out.println("----------------------------------");
            });
        }
    }

    private static void listarAutores() {
        if (livrosBuscados.isEmpty()) {
            System.out.println("Nenhum livro foi buscado ainda.");
            return;
        }

        Set<Autor> autores = new HashSet<>();
        for (Livro livro : livrosBuscados) {
            if (livro.getAuthors() != null && !livro.getAuthors().isEmpty()) {
                autores.add(livro.getAuthors().get(0)); // só o primeiro autor
            }
        }

        if (autores.isEmpty()) {
            System.out.println("Nenhum autor encontrado.");
        } else {
            System.out.println("\n✍️ AUTORES DOS LIVROS BUSCADOS:");
            for (Autor autor : autores) {
                System.out.println(autor);
            }
        }
    }

    private static void listarAutoresVivosEmAno(Scanner scanner) {
        System.out.print("Digite o ano para consulta (ex: 1900): ");
        try {
            int ano = Integer.parseInt(scanner.nextLine().trim());

            Set<Autor> autoresVivos = new HashSet<>();
            for (Livro livro : livrosBuscados) {
                if (livro.getAuthors() != null && !livro.getAuthors().isEmpty()) {
                    Autor autor = livro.getAuthors().get(0);
                    if (autor.getBirthYear() != null &&
                            autor.getBirthYear() <= ano &&
                            (autor.getDeathYear() == null || autor.getDeathYear() > ano)) {
                        autoresVivos.add(autor);
                    }
                }
            }

            if (autoresVivos.isEmpty()) {
                System.out.println("Nenhum autor vivo encontrado no ano " + ano);
            } else {
                System.out.println("\n👤 AUTORES VIVOS EM " + ano + ":");
                autoresVivos.forEach(System.out::println);
            }

        } catch (NumberFormatException e) {
            System.out.println("Ano inválido.");
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

    @JsonAlias("birth_year")
    private Integer birthYear;

    @JsonAlias("death_year")
    private Integer deathYear;

    public String getName() {
        return name;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public Integer getDeathYear() {
        return deathYear;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }

    public void setDeathYear(Integer deathYear) {
        this.deathYear = deathYear;
    }

    @Override
    public String toString() {
        return name + " (Nasc: " + birthYear + ", Morte: " + (deathYear != null ? deathYear : "vivo") + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Autor autor)) return false;
        return Objects.equals(name, autor.name) &&
                Objects.equals(birthYear, autor.birthYear) &&
                Objects.equals(deathYear, autor.deathYear);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, birthYear, deathYear);
    }
}
