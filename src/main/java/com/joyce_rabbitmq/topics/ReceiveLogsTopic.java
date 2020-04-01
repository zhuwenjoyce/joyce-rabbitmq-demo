package com.joyce_rabbitmq.topics;

import com.rabbitmq.client.*;
import com.rabbitmq.client.impl.AMQImpl;

import java.io.IOException;

public class ReceiveLogsTopic {
    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "topic");
        String queueName = channel.queueDeclare().getQueue();

        argv = new String[]{
//                "*.orange.*"   // routing 1  第一次启动main
                "*.*.rabbit", "lazy.#"    // routing 2   第二次启动main
        };

        if (argv.length < 1) {
            System.err.println("Usage: ReceiveLogsTopic [binding_key]...");
            System.exit(1);
        }

        for (String routingKey : argv) {
            channel.queueBind(queueName, EXCHANGE_NAME, routingKey);
            System.err.println("routingKey === " + routingKey);
        }

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        // auto ack = true
//        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
//            String message = new String(delivery.getBody(), "UTF-8");
//            System.out.println(" [x] Received '" +
//                    delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
//        };
//        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });




        // auto ack = false
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag,
                                       Envelope envelope,
                                       AMQP.BasicProperties properties,
                                       byte[] body)
                    throws IOException
            {
                long deliveryTag = envelope.getDeliveryTag();
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + envelope.getRoutingKey() + "' : '" + message + "'");
                // positively acknowledge a single delivery, the message will
                // be discarded
                channel.basicAck(deliveryTag, false);
            }
        };
        channel.basicConsume(queueName, false, "a-consumer-tag",defaultConsumer);
    }
}
