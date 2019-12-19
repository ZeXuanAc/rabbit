package com.zxac.rabbit_consumer.queues;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


/**
 * 忘记通过basicAck返回确认信息是常见的错误。这个错误非常严重，将导致消费者客户端退出或者关闭后，消息会被退回RabbitMQ服务器，这会使RabbitMQ服务器内存爆满，而且RabbitMQ也不会主动删除这些被退回的消息。
 * 如果要监控这种错误，可以使用rabbitmqctl messages_unacknowledged命令打印出出相关的信息。
 * root@czxHost:~# rabbitmqctl list_queues name messages_ready messages_unacknowledged
 */
public class Worker1 {

    private static final String TASK_QUEUE_NAME = "task_queue";

    private final static String RABBIT_HOST = "47.101.198.230";
    private final static int RABBIT_PORT = 5672;

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(RABBIT_HOST);
        factory.setPort(RABBIT_PORT);
        final Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();

        // 第二个参数表示队列需要持久化，不过根据幂次原理已经定义的队列，再次定义是无效的，所以这个队列还是不能持久化，需要重新定义一个新的队列
        channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
        System.out.println("Worker1 [*] Waiting for messages. To exit press CTRL+C");
        // 每次从队列中获取数量,这样做的好处是只有当消费者处理完成当前消息并反馈后，才会收到另外一条消息或任务。这样就避免了负载不均衡的事情了
        channel.basicQos(1);

        final Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, StandardCharsets.UTF_8);

                System.out.println("Worker1 [x] Received '" + message + "'");
                try {
                    doWork(message);
                    // 手动应答消息
                    channel.basicAck(envelope.getDeliveryTag(), false);
                    System.out.println("ack 完成");
                } catch (Exception e) {
                    System.out.println("工人1出故障，消息处理失败, 消息为：" + message);
                    // 手动拒绝消息， 第二个参数为true时将消息重新扔回队列中，false表示直接删除该消息
                    channel.basicReject(envelope.getDeliveryTag(), true);
                }

            }
        };
        // 消息消费完成确认，第二个参数为autoAck,当为true：消费者收到消息自动删除消息，
        // false表示需要消费者手动应答处理（即channel.basicAck(envelope.getDeliveryTag(), false)）后才删除消息
        channel.basicConsume(TASK_QUEUE_NAME, false, consumer);
    }

    private static void doWork(String task) {
        // 模拟故障
        throw new RuntimeException("消息处理失败");
    }

}
