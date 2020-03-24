package controllers;

import controller.CRUDController;
import entity.Client;
import entity.RiskProfile;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import repository.ClientRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        classes = {Config.class},
        loader = AnnotationConfigContextLoader.class)
public class CRUDControllerTest {

    @MockBean
    private ClientRepository clientRepository;

    @Autowired
    private CRUDController crudController;

    private Client client;

    @Before
    public void SetUp() {
        client = new Client();
        client.setId(123);
        client.setRiskProfile(RiskProfile.HIGH);
        when(clientRepository.findById(anyLong())).thenReturn(Optional.of(client));
    }

    @Test
    public void getterTest() {
        long id = 123L;
        Client c = crudController.getClientById(id);
        verify(clientRepository).findById(eq(id));
        assertEquals(c, client);
    }

    @Test
    public void createClientTest() {
        when(clientRepository.findById(anyLong())).thenReturn(Optional.empty());
        crudController.createClient(client);
        verify(clientRepository).save(eq(client));
    }

    @Test(expected = RuntimeException.class)
    public void createExistenceClientTest() {
        crudController.createClient(client);
    }

    @Test
    public void updateClientTest() {
        // create 2 equal array
        List<Client> clients = createRandomClients(20);
        List<Client> clients2 = createRandomClients(20);

        crudController.updateClient(clients);
        verify(clientRepository).saveAll(eq(clients2));
    }

    @Test(expected = RuntimeException.class)
    public void updateClientLostElementTest() {
        // create 2 equal array
        List<Client> clients = createRandomClients(20);

        when(clientRepository.findById(eq((long) (clients.size() / 2)))).thenReturn(Optional.empty());
        crudController.updateClient(clients);
        // Exception should be thrown
        fail();
    }

    @Test
    public void deleteTest() {
        List<Client> clients = createRandomClients(20);
        crudController.delete(clients.stream().map(Client::getId).collect(toList()));

        verify(clientRepository).deleteAllByIdIn(clients.stream().map(Client::getId).collect(toList()));
    }

    private List<Client> createRandomClients(int count) {
        ArrayList<Client> res = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            Client c = new Client();
            c.setId(i);

            switch (i % 3) {
                case 0:
                    c.setRiskProfile(RiskProfile.LOW);
                    break;
                case 1:
                    c.setRiskProfile(RiskProfile.NORMAL);
                    break;
                case 2:
                    c.setRiskProfile(RiskProfile.HIGH);
                    break;
            }

            res.add(c);
        }

        return res;
    }
}
