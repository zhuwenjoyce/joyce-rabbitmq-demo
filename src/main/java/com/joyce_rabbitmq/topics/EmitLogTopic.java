package com.joyce_rabbitmq.topics;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.time.LocalDateTime;

public class EmitLogTopic {
    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.exchangeDeclare(EXCHANGE_NAME, "topic");

            String[] routingKeys = new String[]{
                    "quick.orange.rabbit",     // ReceiveLogsTopic.java 的 routing1 和 routing 2 都可以接收到
                    "lazy.orange.elephant",     // ReceiveLogsTopic.java 的 routing1 和 routing 2 都可以接收到
                    "quick.orange.fox",        // routing1
                    "lazy.brown.fox",          // routing2
                    "lazy.pink.rabbit",        // routing2
                    "quick.brown.fox"
                    ,"quick.orange.male.rabbit"
            };
            int count = 0;
            for (int i = 0; i < routingKeys.length; i++) {
                String routingKey = routingKeys[i];
                String message = "["+(++count)+"]"+LocalDateTime.now().toString();

                channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes("UTF-8"));
                System.out.println(" [x] Sent '" + routingKey + "':'" + message + "'");
                Thread.sleep(500);
            }

        }
    }
}
