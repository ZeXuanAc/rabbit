package com.zxac.rabbit_provider.helloworld;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class P {

    private final static String PROVIDER_NAME = "p1";

    private final static String QUEUE_NAME = "hello";

    private final static String RABBIT_HOST = "47.101.198.230";
    private final static int RABBIT_PORT = 5672;

    public static void main(String[] argv) throws Exception {
        // 创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
//		设置RabbitMQ地址
        factory.setHost(RABBIT_HOST);
        factory.setPort(RABBIT_PORT);
//		创建一个新的连接
        Connection connection = factory.newConnection();
//		创建一个频道
        Channel channel = connection.createChannel();
//		声明一个队列 -- 在RabbitMQ中，队列声明是幂等性的（一个幂等操作的特点是其任意多次执行所产生的影响均与一次执行的影响相同），也就是说，如果不存在，就创建，如果存在，不会对已经存在的队列产生任何影响。
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        String message = "Hello World!【" + PROVIDER_NAME + "】";
        int i = 0;
//		发送消息到队列中
        while (i != 10000000) {
            String mess = message + i;
            try {
                channel.basicPublish("", QUEUE_NAME, null, mess.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(PROVIDER_NAME + "[x] Sent '" + mess + "'");
            i++;
        }

//		关闭频道和连接
        channel.close();
        connection.close();
    }

}
