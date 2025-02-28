package com.ccsw.tutorial.prestamo;

import com.ccsw.tutorial.client.ClientService;
import com.ccsw.tutorial.common.criteria.SearchCriteria;
import com.ccsw.tutorial.game.GameService;
import com.ccsw.tutorial.prestamo.model.Prestamo;
import com.ccsw.tutorial.prestamo.model.PrestamoDto;
import com.ccsw.tutorial.prestamo.model.PrestamoSearchDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;

@Service
@Transactional
public class PrestamoServiceImpl implements PrestamoService {

    @Autowired
    PrestamoRepository prestamoRepository;

    @Autowired
    ClientService clientService;

    @Autowired
    GameService gameService;

    @Override
    public Page<Prestamo> findPage(PrestamoSearchDto dto, Long id_game, Long id_client, String filterDate) {

        PrestamoSpecification gameSpec = new PrestamoSpecification(new SearchCriteria("game.id", ":", id_game));
        PrestamoSpecification clientSpec = new PrestamoSpecification(new SearchCriteria("client.id", ":", id_client));
        //  PrestamoSpecification dateLoanSpec = new PrestamoSpecification(new SearchCriteria("loanDate", "<", filterDate));
        //   PrestamoSpecification dateReturnSpec = new PrestamoSpecification(new SearchCriteria("returnDate", ">", filterDate));
        PrestamoSpecification dateSpec = new PrestamoSpecification(new SearchCriteria("loanDate-returnDate", "between", filterDate));
        Specification<Prestamo> spec = Specification.where(clientSpec).and(gameSpec).and(dateSpec);
        System.out.println(dto);
        return this.prestamoRepository.findAll(spec, dto.getPageable().getPageable());

    }

    @Override
    public void delete(Long id) throws Exception {
        if (this.prestamoRepository.findById(id).orElse(null) == null) {
            throw new Exception("Not Exist");
        }

        this.prestamoRepository.deleteById(id);
    }

    @Override
    public void save(PrestamoDto dto) {
        Prestamo prestamo = new Prestamo();

        BeanUtils.copyProperties(dto, prestamo, "id", "game", "client");
        prestamo.setClient(clientService.get(dto.getClient().getId()));
        prestamo.setGame(gameService.get(dto.getGame().getId()));
        if (checkSave(prestamo)) {

            this.prestamoRepository.save(prestamo);
        }

    }

    public boolean checkSave(Prestamo prestamo) {
        if (prestamo.getLoanDate().isAfter(prestamo.getReturnDate())) {
            return false;
        }
        LocalDate maxReturnDate = prestamo.getLoanDate().plusDays(14);
        if (prestamo.getReturnDate().isAfter(maxReturnDate)) {
            return false;
        }
        
        if (prestamoRepository.countSameClient(prestamo.getGame().getId(), prestamo.getLoanDate(), prestamo.getReturnDate()) > 0) {
            return false;
        }

        ArrayList<Prestamo> prestamosCliente = new ArrayList<>(prestamoRepository.findByClient_id(prestamo.getClient().getId()));

        int count = 0;
        for (Prestamo p : prestamosCliente) {
            if (p.getLoanDate().isBefore(prestamo.getReturnDate()) && p.getReturnDate().isAfter(prestamo.getLoanDate())) {
                count++;
            }

        }
        if (count >= 2) {
            return false;
        }

        return true;
    }

}

