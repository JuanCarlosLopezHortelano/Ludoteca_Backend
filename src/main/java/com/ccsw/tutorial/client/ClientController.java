package com.ccsw.tutorial.client;

import com.ccsw.tutorial.client.model.Client;
import com.ccsw.tutorial.client.model.ClientDto;
import com.ccsw.tutorial.game.model.GameDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Client", description = "API of Client")
@RequestMapping(value = "/client")
@RestController
@CrossOrigin(origins = "*")
public class ClientController {

    // private Map<Long, ClientDto> clients = new HashMap<Long, ClientDto>();

    /**
     * Método para devolver una lista de clientes
     */
    @Operation(summary = "Find", description = "Method to return a list of Clients")
    @RequestMapping(path = "/", method = RequestMethod.GET)
    public List<ClientDto> findAll() {

        // new ArrayList<ClientDto>(this.clients.values());
        return null;
    }

    /**
     * Método para crear o actualizar un {@link Client}
     *
     */
    @Operation(summary = "Save or Update", description = "Method to return a list of Clients")
    @RequestMapping(path = { "", "/{id}" }, method = RequestMethod.PUT)
    public void save(@PathVariable(name = "id", required = false) Long id, @RequestBody GameDto dto) {

        //LLAMAR SERVICE

    }

    /**
     * Metodo para borrar una {@link Client}
     *
     * @param id Pk de la entidad
     */
    @Operation(summary = "Delete", description = "Method that deletes a client")
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Long id) throws Exception {
        // this.clientService.delete(id);
    }
}
