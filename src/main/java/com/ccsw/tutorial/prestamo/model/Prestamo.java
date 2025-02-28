package com.ccsw.tutorial.prestamo.model;

import com.ccsw.tutorial.client.model.Client;
import com.ccsw.tutorial.game.model.Game;
import jakarta.persistence.*;

import java.time.LocalDate;

/**
 * @author ccsw
 */

@Entity
@Table(name = "prestamo")
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_game", nullable = false)
    private Game game;

    @ManyToOne
    @JoinColumn(name = "id_client", nullable = false)
    private Client client;

    @Column(name = "loanDate", nullable = false)
    private LocalDate loanDate;

    @Column(name = "returnDate", nullable = false)
    private LocalDate returnDate;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id new value of {@link #getId}
     *
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return game
     */
    public Game getGame() {
        return this.game;
    }

    /**
     * @param game new value of {@link #getGame}
     *
     */
    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * @return client
     */
    public Client getClient() {
        return this.client;
    }

    /**
     * @param client new value of {@link #getClient}
     *
     */
    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * @return loanDate
     */
    public LocalDate getLoanDate() {
        return loanDate;
    }

    /**
     * @param loanDate new value of {@link #getLoanDate}
     *
     */
    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
    }

    /**
     * @return returnDate
     */
    public LocalDate getReturnDate() {
        return returnDate;
    }

    /**
     * @param returnDate new value of {@link #getReturnDate}
     *
     */
    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
}
