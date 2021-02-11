package nl.feedme.api.serviceTests;

import nl.feedme.api.services.PaypalService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ServiceTestConfiguration.class)
@TestPropertySource(
        locations = "classpath:application.properties")
public class PaypalServiceTest {

    @Autowired
    private PaypalService paypalService;

    @Test
    public void checkIfPropertiesExcists() {
        Assert.assertEquals("AcgcU-pGTho225J0ILL7pvFif2Nq-SbUhXpfIZ1QqzXyJbXvyaqvuW8yKK03TEsbseglJow0Z-gkRN0a", paypalService.getClientId());
    }
}
