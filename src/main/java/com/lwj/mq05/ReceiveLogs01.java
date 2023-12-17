package com.lwj.mq05;

import com.lwj.utils.RabbitMqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * 接收消息
 */
public class ReceiveLogs01 {

    //交换机的名称
    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        //声明一个临时队列
        String queueName = channel.queueDeclare().getQueue();
        /**
         * 绑定交换机
         */
        channel.queueBind(queueName, EXCHANGE_NAME, "");
        System.out.println("01等待接收消息");

        //接收消息
        DeliverCallback deliverCallback = (consumerTag, message ) -> {
            System.out.println("01接收到的消息 ：" + new String(message.getBody()));
        };
        //接收中断
        CancelCallback cancelCallback = (v1) -> {};
        channel.basicConsume(queueName, true, deliverCallback,cancelCallback);
    }
}
