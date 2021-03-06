package com.bunnu.messaging;

import java.io.FileInputStream;
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.springframework.jms.core.JmsTemplate;

public class RecieveMessage {
	public static void main(String[] args) {
		RecieveMessage hello = new RecieveMessage();
		MessageDto dto = new MessageDto();
		hello.runTest(dto);
	}

	private void runTest(MessageDto dto) {
		Properties properties = new Properties();
		try {
			properties.load(
					new FileInputStream("C:/Users/ISH/workspace/SpringBootSource/src/main/resources/hello.properties"));

			Context context = new InitialContext(properties);

			ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup("qpidConnectionfactory");

			Connection connection = connectionFactory.createConnection();
			connection.start();
			Destination destination = (Destination) context.lookup("topicExchange");
			// TextMessage message = session.createTextMessage("Hello world!");
			recieveMessage(connectionFactory, destination);
			connection.close();
			context.close();

		} catch (Exception exp) {
			exp.printStackTrace();
		}

	}

	private void recieveMessage(ConnectionFactory connectionFactory, Destination destination) {
		QpidMessageConverter converter = new QpidMessageConverter();
		JmsTemplate template = new JmsTemplate(connectionFactory);
		template.setDefaultDestination(destination);
		template.setMessageConverter(converter);
		MessageDto dto2 = (MessageDto) template.receiveAndConvert();
		System.out.println("RecievedSuccessfully" + dto2);
	}
}
