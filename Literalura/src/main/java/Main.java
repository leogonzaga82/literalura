import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

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
                    System.out.println(livro);  // usa o toString()
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

    @Override
    public String toString() {
        return "RespostaApi{" +
                "results=" + results +
                '}';
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

    public int getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(int downloadCount) {
        this.downloadCount = downloadCount;
    }

    @Override
    public String toString() {
        String autoresStr = (authors != null && !authors.isEmpty())
                ? authors.get(0).getName()
                : "Autor desconhecido";

        return "Título: " + title + "\n" +
                "Autor: " + autoresStr + "\n" +
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
