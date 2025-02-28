package com.ccsw.tutorial.prestamo;

import com.ccsw.tutorial.prestamo.model.Prestamo;
import com.ccsw.tutorial.prestamo.model.PrestamoDto;
import com.ccsw.tutorial.prestamo.model.PrestamoSearchDto;
import org.springframework.data.domain.Page;

public interface PrestamoService {

    /**
     * Metodo para recuperar un listado paginado de {@link Prestamo}
     *
     * @param dto dto de búsqueda
     * @return {@link Page} de {@link Prestamo}
     */

    Page<Prestamo> findPage(PrestamoSearchDto dto, Long id_game, Long id_client, String filterDate);

    /**
     * Metodo para eliminar un Prestamo
     *
     * @param id PK de la entidad
     */

    void delete(Long id) throws Exception;

    /**
     * Guarda un prestamo
     *
     * @param dto datos de la entidad
     */
    void save(PrestamoDto dto);

    boolean checkSave(Prestamo prestamo);
}
