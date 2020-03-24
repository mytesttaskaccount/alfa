package controller;

import entity.Client;
import entity.RiskProfile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import repository.ClientRepository;

import javax.validation.constraints.NotNull;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Controller
@ResponseBody
public class ProcessingController {

    private final ClientRepository clientRepository;

    public ProcessingController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @PutMapping("/unite")
    public void uniteClients(@NotNull @RequestParam List<Long> ids) {
        List<Client> clients = ids.stream()
                .map(id -> clientRepository.findById(id).orElseThrow(() -> new RuntimeException("Not all clients do exist in DB")))
                .collect(toList());

        RiskProfile max = clients.stream().map(Client::getRiskProfile)
                .max(Enum::compareTo)
                .orElseThrow(() -> new RuntimeException("IDs list must not be empty"));

        for (Client c : clients) {
            c.setRiskProfile(max);
        }
        clientRepository.saveAll(clients);
    }
}
