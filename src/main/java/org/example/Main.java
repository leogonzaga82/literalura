package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Component
    public class AppRunner implements CommandLineRunner {

        @Autowired
        private LivroRepository livroRepository;

        @Autowired
        private AutorRepository autorRepository;

        @Override
        public void run(String... args) throws Exception {
            // Limpando o banco
            livroRepository.deleteAll();
            autorRepository.deleteAll();

            // Criando e salvando autores
            Autor orwell = autorRepository.save(new Autor("George Orwell"));
            Autor machado = autorRepository.save(new Autor("Machado de Assis"));

            // Criando livros com autores já salvos
            Livro livro1 = new Livro("1984", 1949, "inglês", orwell);
            Livro livro2 = new Livro("Animal Farm", 1945, "inglês", orwell);
            Livro livro3 = new Livro("Dom Casmurro", 1899, "português", machado);
            Livro livro4 = new Livro("Memórias Póstumas de Brás Cubas", 1881, "português", machado);

            // Salvando os livros
            livroRepository.save(livro1);
            livroRepository.save(livro2);
            livroRepository.save(livro3);
            livroRepository.save(livro4);

            // Contando os livros por idioma
            long countIngles = livroRepository.countByIdioma("inglês");
            long countPortugues = livroRepository.countByIdioma("português");

            // Mostrando no console
            System.out.println("Livros em inglês: " + countIngles);
            System.out.println("Livros em português: " + countPortugues);
        }
    }
}
