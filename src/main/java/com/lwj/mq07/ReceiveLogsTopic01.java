package com.lwj.mq07;

import com.lwj.utils.RabbitMqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * 声明主题交换机及队列
 * 消费者01
 */
public class ReceiveLogsTopic01 {
    public static final String EXCHANGE_NAME = "topic_logs";
    public static final String EXCHANGE_TYPE = "topic";

    //接收消息
    public static void main(String[] args) throws Exception {
        //获得信道
        Channel channel = RabbitMqUtils.getChannel();
        //声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE);
        //声明队列
        String queueName = "Q1";
        channel.queueDeclare(queueName, false, false, false, null);
        //绑定
        channel.queueBind(queueName, EXCHANGE_NAME, "*.orange.*");
        System.out.println("01等待接收消息");

        //接收消息
        DeliverCallback deliverCallback = (consumerTag, message) ->{
            System.out.println("01接收消息：" + new String(message.getBody()) +
                    "\n接收队列：" + queueName + "\t绑定键：" + message.getEnvelope().getRoutingKey());
        };
        CancelCallback cancelCallback =consumerTag ->{};
        channel.basicConsume(queueName, true,deliverCallback, cancelCallback);
    }
}
