package org.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    CommandLineRunner init(AutorRepository autorRepo, LivroRepository livroRepo) {
        return args -> {
            Autor autor = new Autor("George Orwell");
            Livro livro = new Livro("1984", 1949, autor);

            livroRepo.save(livro);

            List<Livro> livros = livroRepo.findAll();
            for (Livro l : livros) {
                System.out.println("Livro: " + l.getTitulo() + ", Ano: " + l.getAno() + ", Autor: " + l.getAutor().getNome());
            }
        };
    }
}