# joyce-rabbitmq-demo

###  1) Hello World
package "com.joyce_rabbitmq.hello_world" demo is from 
https://www.rabbitmq.com/tutorials/tutorial-one-java.html
<br/>
演示了最简单服务端和客户端demo

###  2) Work queues
package "com.joyce_rabbitmq.work_queues" demo is from 
https://www.rabbitmq.com/tutorials/tutorial-two-java.html
<br/>
工作队列模式：启动多个Worker Main方法，演示在多个客户端情况下，rabbitmq能做到客户端的消息负载均衡。

###  3) Publish/Subscribe 
package "com.joyce_rabbitmq.publish_subscribe" demo is from 
https://www.rabbitmq.com/tutorials/tutorial-three-java.html
<br/>
发布/订阅模式：所有订阅者都将获得消息。启动多次ReceiveLogs.java，模拟多个客户端同时处理消息。
<br/>
exchange有四种方式： direct,topic,headers,fanout. 这里演示的是fanout方式。

###  4) Routing 
package "com.joyce_rabbitmq.routing" demo is from 
https://www.rabbitmq.com/tutorials/tutorial-four-java.html
<br/>
路由模式：消费者可以通过routing路由只消费自己想监听的数据，如果一条消息没有被任何消费者监听，则会被丢弃。



