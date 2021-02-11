package nl.feedme.api.serviceTests;

import nl.feedme.api.models.Chef;
import nl.feedme.api.repositories.ChefRepository;
import nl.feedme.api.services.ChefService;
import nl.feedme.api.services.QRGeneratorService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ServiceTestConfiguration.class)
public class ChefServiceTest {

    @Autowired
    private ChefService chefService;

    @Autowired
    private ChefRepository chefRepository;

    @Before
    public void setUp() {
        Chef cheffie = new Chef("Lars", "Lars");
        Mockito.when(chefRepository.findChefByUsername(cheffie.getUsername()))
                .thenReturn(cheffie);
    }

    @Test
    public void getCheffie() {
        UserDetails cheffie = chefService.loadUserByUsername("Lars");

        Assert.assertEquals("Lars", cheffie.getUsername());
    }
}
