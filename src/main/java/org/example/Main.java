package org.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    public CommandLineRunner run(AutorRepository autorRepository, LivroRepository livroRepository) {
        return args -> {
            // Apaga dados antigos pra facilitar testes
            livroRepository.deleteAll();
            autorRepository.deleteAll();

            // Cria autores com ano nascimento e falecimento (null = vivo)
            Autor orwell = new Autor("George Orwell", 1903, 1950);
            Autor machado = new Autor("Machado de Assis", 1839, 1908);
            Autor king = new Autor("Stephen King", 1947, null); // vivo

            autorRepository.saveAll(List.of(orwell, machado, king));

            // Cria livros associados aos autores
            livroRepository.save(new Livro("1984", 1949, "en", orwell));
            livroRepository.save(new Livro("A Revolução dos Bichos", 1945, "en", orwell));
            livroRepository.save(new Livro("Dom Casmurro", 1899, "pt", machado));
            livroRepository.save(new Livro("It", 1986, "en", king));

            // Pede ano para o usuário e lista autores vivos nesse ano
            Scanner scanner = new Scanner(System.in);
            System.out.print("Digite um ano para ver autores vivos: ");

            try {
                int ano = Integer.parseInt(scanner.nextLine());

                List<Autor> autoresVivos = autorRepository.buscarAutoresVivosNoAno(ano);

                if (autoresVivos.isEmpty()) {
                    System.out.println("Nenhum autor estava vivo em " + ano);
                } else {
                    System.out.println("Autores vivos em " + ano + ":");
                    for (Autor autor : autoresVivos) {
                        System.out.println("- " + autor.getNome());
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Ano inválido. Digite um número inteiro.");
            }
        };
    }
}
