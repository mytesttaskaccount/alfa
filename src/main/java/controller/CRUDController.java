package controller;

import entity.Client;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import repository.ClientRepository;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.List;

;

@Transactional
@Controller
@ResponseBody
public class CRUDController {

    private final ClientRepository clientRepository;

    public CRUDController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }


    @GetMapping("/{id}")
    public Client getClientById(@PathVariable long id) {
        return clientRepository.findById(id).orElse(null);
    }

    @PostMapping
    public void createClient(@NotNull @RequestBody Client c) {
        if (clientRepository.findById(c.getId()).isPresent()) {
            throw new RuntimeException("Clinet with ID " + c.getId() + " does already exist");
        }
        clientRepository.save(c);
    }

    @PutMapping("/")
    public void updateClient(@RequestBody List<Client> clients) {
        // check that all clients are present in db
        clients.stream().map(Client::getId).forEach(id -> clientRepository.findById(id).orElseThrow(() -> new RuntimeException("Not all clients do exist in DB")));

        // save all
        clientRepository.saveAll(clients);
    }

    @DeleteMapping("/")
    public void delete(@RequestBody List<Long> ids) {
        clientRepository.deleteAllByIdIn(ids);
    }
}
