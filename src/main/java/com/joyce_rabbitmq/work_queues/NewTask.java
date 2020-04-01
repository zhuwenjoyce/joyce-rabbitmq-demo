package com.joyce_rabbitmq.work_queues;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.time.LocalDateTime;

public class NewTask {

    private static final String TASK_QUEUE_NAME = "task_queue";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);

            for (int i = 0; i < 10; i++) {
                String message = String.join(" ", argv);
                if(i == 0){
                    message = "LongTime......"; // 6个点，所以客户端要处理6秒
                }else {
                    message = "ShortTime.";   // 1 个点，所以客户端只需处理1秒，这样处理快的客户端能更快速接收处理下一个消息，做到客户端负载均衡
                }
                message = LocalDateTime.now() + ":::" + message;

                channel.basicPublish("", TASK_QUEUE_NAME,
                        MessageProperties.PERSISTENT_TEXT_PLAIN,
                        message.getBytes("UTF-8"));
                System.out.println(" [x] Sent '" + message + "'");
            }
        }
    }

}
