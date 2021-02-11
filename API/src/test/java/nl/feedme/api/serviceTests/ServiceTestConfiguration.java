package nl.feedme.api.serviceTests;

import nl.feedme.api.repositories.*;
import nl.feedme.api.services.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@TestConfiguration
public class ServiceTestConfiguration {

    @Bean
    public RestaurantTableService restaurantTableService() {
        return new RestaurantTableService();
    }

    @Bean
    public GuestService guestService() {
        return new GuestService();
    }

    @Bean
    public OrderService orderService() {
        return new OrderService();
    }

    @Bean
    public ConsumptionService consumptionService() { return new ConsumptionService(); }

    @Qualifier("QRGeneratorService")
    @Bean
    public QRGeneratorService qrGeneratorService() { return new QRGeneratorService(); }

    @Bean
    public ChefService chefService() {return new ChefService(); }

    @Qualifier("passwordEncoder")
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean PaypalService paypalService() { return new PaypalService(); }

    // Mocking the repositories since we're only testing the services here
    @Bean
    public GuestRepository guestRepository() {return Mockito.mock(GuestRepository.class); }

    @Bean
    public RestaurantTableRepository restaurantTableRepository() { return Mockito.mock(RestaurantTableRepository.class); }

    @Bean
    public OrderRepository orderRepository() {return Mockito.mock(OrderRepository.class); }

    @Bean
    public ConsumptionRepository consumptionRepository() { return Mockito.mock(ConsumptionRepository.class) ;}

    @Bean
    public ChefRepository chefRepository() { return Mockito.mock(ChefRepository.class); }
}
