package com.joyce_rabbitmq.routing;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Random;

public class EmitLogDirect {
    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (
                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel();
                InputStream is = System.in;
        ) {
            channel.exchangeDeclare(EXCHANGE_NAME, "direct");

            String routingKey = "error";
            String message = "eeeee";
            for (int i = 0; i < 10; i++) {
                int num = new Random().nextInt(100);
                if(num % 10 == 0){
                    routingKey = "warning";  // 被监听的routingKey，会被客户端消费
                    message = "wwwww-"+ LocalDateTime.now();
                }if(num % 5 == 0){
                    routingKey = "other";  // 未被监听的routingKey，消息会被丢弃
                    message = "oooo-"+ LocalDateTime.now();
                }else if(num % 3 == 0){
                    routingKey = "error";
                    message = "eeee-"+ LocalDateTime.now();
                }else{
                    routingKey = "info";
                    message = "iiii-"+ LocalDateTime.now();
                }
                channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes("UTF-8"));
                System.out.println(" [x] Sent '" + routingKey + "':'" + message + "'");
            }
        }
    }
}
