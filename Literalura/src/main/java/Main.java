import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String url = "https://gutendex.com/books/?search=tolstoy";

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

                System.out.println("📚 LIVROS ENCONTRADOS:");
                for (Livro livro : livros) {
                    System.out.println("Título: " + livro.getTitle());

                    if (livro.getAuthors() != null && !livro.getAuthors().isEmpty()) {
                        System.out.println("Autor: " + livro.getAuthors().get(0).getName());
                    }

                    System.out.println("Idiomas: " + livro.getLanguages());
                    System.out.println("Downloads: " + livro.getDownload_count());
                    System.out.println("----------------------------------");
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
    private int download_count;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Autor> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Autor> authors) {
        this.authors = authors;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public int getDownload_count() {
        return download_count;
    }

    public void setDownload_count(int download_count) {
        this.download_count = download_count;
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
}
