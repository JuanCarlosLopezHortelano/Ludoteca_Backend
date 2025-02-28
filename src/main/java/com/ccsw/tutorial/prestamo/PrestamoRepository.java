package com.ccsw.tutorial.prestamo;

import com.ccsw.tutorial.prestamo.model.Prestamo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PrestamoRepository extends CrudRepository<Prestamo, Long>, JpaSpecificationExecutor<Prestamo> {

    @Override
    @EntityGraph(attributePaths = { "client", "game" })
    Page<Prestamo> findAll(Specification<Prestamo> spec, Pageable pageable);

    List<Prestamo> findByClient_id(Long ClientId);

    @Query("""
            Select count(p)
            From Prestamo p where p.game.id = :id_game
            AND p.loanDate <= :newReturnDate
            AND p.returnDate>= :newLoanDate
            """)
    Long countSameClient(@Param("id_game") Long id_game, @Param("newLoanDate") LocalDate newLoanDate, @Param("newReturnDate") LocalDate newReturnDate);

}