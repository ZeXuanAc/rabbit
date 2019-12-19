package com.zxac.rabbit_consumer.publish;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ReceiveLogs1 {
    private static final String EXCHANGE_NAME = "logs";

//    private static final String TASK_QUEUE_NAME = "log_queue";

    private final static String RABBIT_HOST = "47.101.198.230";
    private final static int RABBIT_PORT = 5672;

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(RABBIT_HOST);
        factory.setPort(RABBIT_PORT);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        // 这里如果只定义一个队列的话，即使分发模式下队列中的数据也只有一份，所以这里使用临时队列来生成对应的数据，
        // 因为如果一个交换器绑定同一个队列，两个消费者监听同一队列的话，一个消费者消费了消息就处理掉了，达不到分发的作用
//        channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
        // queueDeclare()为我们创建一个非持久化、独立、自动删除的队列名称
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, EXCHANGE_NAME, "");

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, StandardCharsets.UTF_8);
                System.out.println(" [x] Received '" + message + "'");
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }
}