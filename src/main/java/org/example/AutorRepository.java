package org.example;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    // Pode ficar vazio se não precisar de método extra
}
