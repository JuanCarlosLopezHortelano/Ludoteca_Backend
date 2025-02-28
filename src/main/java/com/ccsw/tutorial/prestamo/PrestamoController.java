package com.ccsw.tutorial.prestamo;

import com.ccsw.tutorial.prestamo.model.Prestamo;
import com.ccsw.tutorial.prestamo.model.PrestamoDto;
import com.ccsw.tutorial.prestamo.model.PrestamoSearchDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@Tag(name = "Prestamo", description = "API of Prestamo")
@RequestMapping(value = "/prestamo")
@RestController()
@CrossOrigin(origins = "*")

public class PrestamoController {

    @Autowired
    PrestamoService prestamoService;

    @Autowired
    ModelMapper mapper;

    /**
     * Metodo para recuperar un listado paginado de {@link Prestamo}
     *
     * @param dto dto de busqueda
     * @return {@link Page} de {@link PrestamoDto}
     *
     */
    @Operation(summary = "Find Page", description = "Method that return a page of Prestamos")
    @RequestMapping(path = "", method = RequestMethod.POST)
    public Page<PrestamoDto> findPage(@RequestBody PrestamoSearchDto dto, @RequestParam(value = "id_game", required = false) Long id_game, @RequestParam(value = "id_client", required = false) Long id_client,
            @RequestParam(value = "filterDate", required = false) String filterDate) {

        Page<Prestamo> page = this.prestamoService.findPage(dto, id_game, id_client, filterDate);

        return new PageImpl<>(page.getContent().stream().map(e -> mapper.map(e, PrestamoDto.class)).collect(Collectors.toList()), page.getPageable(), page.getTotalElements());

    }

    /**
     * Metodo para eliminar un  {@link Prestamo}
     *
     * @param id de la entidad
     */
    @Operation(summary = "Delete", description = "Method that deletes a Prestamo")
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Long id) throws Exception {
        this.prestamoService.delete(id);
    }

    /**
     * Método para crear  un {@link Prestamo}
     *
     * @param dto datos de la entidad
     */
    @Operation(summary = "Save ", description = "Method that saves a Prestamo")
    @RequestMapping(path = { "" }, method = RequestMethod.PUT)
    public void save(@RequestBody PrestamoDto dto) {
        this.prestamoService.save(dto);

    }
}
