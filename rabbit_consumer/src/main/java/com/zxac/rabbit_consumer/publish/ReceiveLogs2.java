package com.zxac.rabbit_consumer.publish;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ReceiveLogs2 {
    private static final String EXCHANGE_NAME = "logs";

    private static final String TASK_QUEUE_NAME = "log_queue";

    private final static String RABBIT_HOST = "47.101.198.230";
    private final static int RABBIT_PORT = 5672;

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(RABBIT_HOST);
        factory.setPort(RABBIT_PORT);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, EXCHANGE_NAME, "");

        System.out.println(" [*] Waiting2 for messages. To exit press CTRL+C");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, StandardCharsets.UTF_8);
                System.out.println(" [x2] Received '" + message + "'");
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }
}