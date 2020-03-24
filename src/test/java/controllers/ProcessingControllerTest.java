package controllers;

import controller.ProcessingController;
import entity.Client;
import entity.RiskProfile;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import repository.ClientRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static entity.RiskProfile.HIGH;
import static entity.RiskProfile.LOW;
import static entity.RiskProfile.NORMAL;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        classes = {Config.class},
        loader = AnnotationConfigContextLoader.class)
public class ProcessingControllerTest {

    @MockBean
    private ClientRepository clientRepository;

    @Autowired
    private ProcessingController processingController;

    private List<Client> clients;
    private int initCapacity = 20;

    @Before
    public void SetUp() {
        clients = createClients(initCapacity, LOW);
        when(clientRepository.findById(anyLong())).then((Answer<Optional>) invocationOnMock -> {
            long i = invocationOnMock.getArgument(0);
            return Optional.of(clients.get((int)i));
        });
    }

    @Test
    public void uniteClientsHalfDBTest() {
        processingController.uniteClients(clients.stream().skip(clients.size() / 2).map(Client::getId).collect(toList()));
        verify(clientRepository).saveAll(eq(clients.stream().skip(clients.size() / 2).collect(toList())));
    }

    @Test
    public void uniteClientsOneNormalTest() {
        clients.get(clients.size() - 1).setRiskProfile(NORMAL);

        processingController.uniteClients(clients.stream().map(Client::getId).collect(toList()));

        List<Client> res = createClients(initCapacity, NORMAL);
        verify(clientRepository).saveAll(eq(res));
    }

    @Test
    public void uniteClientsOneHighTest() {
        clients.get(0).setRiskProfile(HIGH);

        processingController.uniteClients(clients.stream().map(Client::getId).collect(toList()));

        List<Client> res = createClients(initCapacity, HIGH);
        verify(clientRepository).saveAll(eq(res));
    }

    @Test
    public void uniteClientsOneLowTest() {
        clients.forEach(c -> c.setRiskProfile(HIGH));
        clients.get(clients.size() - 1).setRiskProfile(LOW);

        processingController.uniteClients(clients.stream().map(Client::getId).collect(toList()));

        List<Client> res = createClients(initCapacity, HIGH);
        verify(clientRepository).saveAll(eq(res));
    }

    @Test(expected = RuntimeException.class)
    public void emptyListTest() {
        processingController.uniteClients(Collections.emptyList());
        fail();
    }

    @Test(expected = RuntimeException.class)
    public void ClientNotFoundTest() {

        when(clientRepository.findById(eq((long)clients.size() - 1))).thenReturn(Optional.empty());

        processingController.uniteClients(clients.stream().map(Client::getId).collect(toList()));
        fail();
    }

    private List<Client> createClients(int count, RiskProfile riskProfile) {
        ArrayList<Client> res = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            Client c = new Client();
            c.setId(i);
            c.setRiskProfile(riskProfile);

            res.add(c);
        }

        return res;
    }
}
