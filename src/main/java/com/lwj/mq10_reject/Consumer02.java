package com.lwj.mq10_reject;

import com.lwj.utils.RabbitMqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * 死信队列
 * 消费者 死信
 * 模拟队列到达最长
 */
public class Consumer02 {
    //死信队列
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        System.out.println("02等待接收消息");
        //接收消息
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("02接收消息：" + new String(message.getBody(),"UTF-8"));
        };
        CancelCallback cancelCallback = consumerTag -> {};
        channel.basicConsume(DEAD_QUEUE, true, deliverCallback,cancelCallback);
    }
}
