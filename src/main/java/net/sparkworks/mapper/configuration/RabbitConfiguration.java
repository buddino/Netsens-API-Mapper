package net.sparkworks.mapper.configuration;

import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

@Configuration
@PropertySource(value = "file:application.properties", ignoreResourceNotFound = false)
public class RabbitConfiguration {

    private static final Logger LOGGER = Logger.getLogger(RabbitConfiguration.class);

    @Value("${rabbitmq.server}")
    String rabbitServer;
    @Value("${rabbitmq.port}")
    String rabbitPort;
    @Value("${rabbitmq.username}")
    String rabbitUser;
    @Value("${rabbitmq.password}")
    String rabbitPassword;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    ConnectionFactory connectionFactory() throws KeyManagementException, NoSuchAlgorithmException {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(rabbitServer);
        connectionFactory.setPort(Integer.parseInt(rabbitPort));
        connectionFactory.setUsername(rabbitUser);
        connectionFactory.setPassword(rabbitPassword);
        return connectionFactory;
    }

}