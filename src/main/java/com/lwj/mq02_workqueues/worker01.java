package com.lwj.mq02_workqueues;

import com.lwj.utils.RabbitMqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

public class worker01 {

    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        //工具类创建信道
        Channel channel = RabbitMqUtils.getChannel();

        System.out.println("01等待接收消息...");

        //声明接收消息
        DeliverCallback deliverCallback = (consumerTag,message) -> {
            System.out.println( "01: " + new String(message.getBody()));
        };

        //接收消息回调
        CancelCallback cancelCallback = consumerTag -> {
            System.out.println("01消息消费中断");
        };
        //接收消息
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);

    }
}
