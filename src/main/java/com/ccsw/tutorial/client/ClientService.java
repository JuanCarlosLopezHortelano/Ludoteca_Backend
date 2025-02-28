package com.ccsw.tutorial.client;

import com.ccsw.tutorial.client.model.Client;
import com.ccsw.tutorial.client.model.ClientDto;

import java.util.List;

public interface ClientService {

    /**
     * Método para recuperar todos los clientes
     *
     * @return {@link List} de {@link Client}
     *
     */
    List<Client> findAll();

    /**
     *Método para crear o actualizar un cliente
     *
     * @param id PK de la entindad
     * @param dto datos de la entidad
     */

    void save(Long id, ClientDto dto) throws Exception;

    Client exist(String name);

    /**
     *Método para eliminar un cliente
     *
     * @param id PK de la entindad
     *
     */
    void delete(Long id) throws Exception;

    Client get(Long id);

}
