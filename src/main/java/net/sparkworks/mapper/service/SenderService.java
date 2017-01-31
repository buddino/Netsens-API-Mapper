package net.sparkworks.mapper.service;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Locale;

@Service
public class SenderService {
    private static final Logger LOGGER = Logger.getLogger(SenderService.class);
	private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private static final String MESSAGE_TEMPLATE = "%s,%f,%d";

    @Value("${rabbitmq.queue.send}")
    String rabbitQueueSend;

    @Autowired
    RabbitTemplate rabbitTemplate;

	//Removed Async to maintain temporal chronological order
	//@Async
	public void sendMeasurement(final String uri, final Double reading, final long timestamp) {
        final String message = String.format(Locale.US, MESSAGE_TEMPLATE, uri, reading, timestamp);
		LOGGER.debug(String.format("%s %.2f %s", uri, reading, sdf.format(timestamp)));
		rabbitTemplate.send(rabbitQueueSend, rabbitQueueSend, new Message(message.getBytes(), new MessageProperties()));
    }


}
