package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AppRunner implements CommandLineRunner {

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private AutorRepository autorRepository;

    @Override
    public void run(String... args) throws Exception {
        // Limpa dados antes para não duplicar
        livroRepository.deleteAll();
        autorRepository.deleteAll();

        // Salva autores no banco
        Autor orwell = new Autor("George Orwell", 1903, 1950);
        Autor machado = new Autor("Machado de Assis", 1839, 1908);

        // Cria livros referenciando autores persistidos
        Livro livro1 = new Livro("1984", 1949, "inglês", orwell);
        Livro livro2 = new Livro("Animal Farm", 1945, "inglês", orwell);
        Livro livro3 = new Livro("Dom Casmurro", 1899, "português", machado);
        Livro livro4 = new Livro("Memórias Póstumas de Brás Cubas", 1881, "português", machado);

        // Salva livros
        livroRepository.save(livro1);
        livroRepository.save(livro2);
        livroRepository.save(livro3);
        livroRepository.save(livro4);

        // Conta livros por idioma e imprime no console
        long countIngles = livroRepository.countByIdioma("inglês");
        long countPortugues = livroRepository.countByIdioma("português");

        System.out.println("Livros em inglês: " + countIngles);
        System.out.println("Livros em português: " + countPortugues);
    }
}
