package com.ccsw.tutorial.prestamo;

import com.ccsw.tutorial.client.model.Client;
import com.ccsw.tutorial.game.model.Game;
import com.ccsw.tutorial.prestamo.model.Prestamo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PrestamoTest {

    @Mock
    PrestamoRepository prestamoRepository;

    @InjectMocks
    PrestamoServiceImpl prestamoService;

    @Test
    public void checkSaveShouldReturnFalseIfLoanDateAfterReturnDate() {

        Prestamo prestamo = new Prestamo();
        prestamo.setLoanDate(LocalDate.now().plusDays(1));
        prestamo.setReturnDate(LocalDate.now());

        boolean result = prestamoService.checkSave(prestamo);

        assertFalse(result);
    }

    @Test
    public void checkSaveShouldReturnFalseIfReturnDate15DaysSinceLoanDate() {

        Prestamo prestamo = new Prestamo();
        prestamo.setLoanDate(LocalDate.now());
        prestamo.setReturnDate(LocalDate.now().plusDays(15));

        boolean result = prestamoService.checkSave(prestamo);

        assertFalse(result);
    }

    @Test
    public void checkSaveShouldReturnFalseIfGameIsLentToTwoPeople() {

        Prestamo prestamo = new Prestamo();
        prestamo.setLoanDate(LocalDate.now());
        prestamo.setReturnDate(LocalDate.now().plusDays(7));

        Game game = new Game();
        game.setId(1L);
        prestamo.setGame(game);

        when(prestamoRepository.countSameClient(game.getId(), prestamo.getLoanDate(), prestamo.getReturnDate())).thenReturn(1L);

        boolean result = prestamoService.checkSave(prestamo);

        assertFalse(result);
    }

    @Test
    public void checkSaveShouldReturnFalseIfClientHasMoreThanTwoLoans() {

        Prestamo prestamo = new Prestamo();
        prestamo.setLoanDate(LocalDate.now());
        prestamo.setReturnDate(LocalDate.now().plusDays(7));

        Client client = new Client();
        client.setId(1L);
        prestamo.setClient(client);

        Game game = new Game();
        game.setId(1L);
        prestamo.setGame(game);

        Prestamo Loan1 = new Prestamo();
        Loan1.setLoanDate(LocalDate.now());
        Loan1.setReturnDate(LocalDate.now().plusDays(1));

        Prestamo Loan2 = new Prestamo();
        Loan2.setLoanDate(LocalDate.now());
        Loan2.setReturnDate(LocalDate.now().plusDays(8));

        ArrayList<Prestamo> prestamosCliente = new ArrayList<>();
        prestamosCliente.add(Loan1);
        prestamosCliente.add(Loan2);

        when(prestamoRepository.findByClient_id(client.getId())).thenReturn(prestamosCliente);

        boolean result = prestamoService.checkSave(prestamo);

        assertFalse(result);
    }

    @Test
    public void checkSaveShouldReturnTrueIfAllConditionsAreMet() {

        Prestamo prestamo = new Prestamo();
        prestamo.setLoanDate(LocalDate.now());
        prestamo.setReturnDate(LocalDate.now().plusDays(7));

        Client client = new Client();
        client.setId(1L);
        prestamo.setClient(client);

        Game game = new Game();
        game.setId(1L);
        prestamo.setGame(game);

        when(prestamoRepository.countSameClient(game.getId(), prestamo.getLoanDate(), prestamo.getReturnDate())).thenReturn(0L);
        when(prestamoRepository.findByClient_id(client.getId())).thenReturn(new ArrayList<>());

        boolean result = prestamoService.checkSave(prestamo);

        assertTrue(result);

    }
}