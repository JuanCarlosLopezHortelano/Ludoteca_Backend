package com.ccsw.tutorial.client;

import com.ccsw.tutorial.client.model.Client;
import com.ccsw.tutorial.client.model.ClientDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ClientServiceImpl implements ClientService {

    @Autowired
    ClientRepository clientRepository;

    @Override
    public List<Client> findAll() {
        return (List<Client>) this.clientRepository.findAll();
    }

    @Override
    public Client get(Long id) {
        return this.clientRepository.findById(id).orElse(null);
    }

    @Override
    public void save(Long id, ClientDto dto) throws Exception {
        if (this.exist(dto.getName()) != null) {
            throw new Exception("El nombre ya esta siendo usado");
        }

        Client client;
        if (id == null) {
            client = new Client();
        } else {
            client = get(id);
        }

        client.setName(dto.getName());
        clientRepository.save(client);
    }

    @Override
    public Client exist(String name) {
        return this.clientRepository.findByName(name).orElse(null);
    }

    @Override
    public void delete(Long id) throws Exception {
        if (this.get(id) == null) {
            throw new Exception("No existe");
        }
        clientRepository.delete(this.get(id));
    }
}
