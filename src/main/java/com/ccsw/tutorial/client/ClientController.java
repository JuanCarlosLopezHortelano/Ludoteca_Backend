package com.ccsw.tutorial.client;

import com.ccsw.tutorial.client.model.Client;
import com.ccsw.tutorial.client.model.ClientDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Client", description = "API of Client")
@RequestMapping(value = "/client")
@RestController
@CrossOrigin(origins = "*")
public class ClientController {

    @Autowired
    ClientService clientService;

    @Autowired
    ModelMapper mapper;

    /**
     * Método para devolver una lista de clientes
     */
    @Operation(summary = "Find", description = "Method to return a list of Clients")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<ClientDto> findAll() {
        List<Client> clients = this.clientService.findAll();
        return clients.stream().map(e -> mapper.map(e, ClientDto.class)).collect(Collectors.toList());
    }

    /**
     * Método para crear o actualizar un {@link Client}
     *
     */
    @Operation(summary = "Save or Update", description = "Method to return a list of Clients")
    @RequestMapping(path = { "", "/{id}" }, method = RequestMethod.PUT)
    public void save(@PathVariable(name = "id", required = false) Long id, @RequestBody ClientDto dto) throws Exception {

        clientService.save(id, dto);

    }

    /**
     * Metodo para borrar una {@link Client}
     *
     * @param id Pk de la entidad
     */
    @Operation(summary = "Delete", description = "Method that deletes a client")
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Long id) throws Exception {
        this.clientService.delete(id);
    }

    /**
     * Método para devolver una lista de clientes
     */
    @Operation(summary = "Exist", description = "Method to return a Client")
    @RequestMapping(path = "/{name}", method = RequestMethod.GET)
    public boolean existName(@PathVariable("name") String name) {
        Client client = this.clientService.exist(name);
        return client != null;
    }

}
