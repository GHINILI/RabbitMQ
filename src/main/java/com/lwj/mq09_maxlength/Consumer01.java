package com.lwj.mq09_maxlength;

import com.lwj.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.util.HashMap;

/**
 *死信队列
 * 消费者 普通
 * 模拟队列到达最长
 */
public class Consumer01 {
    //普通交换机名称
    private static final String NORMAL_EXCHANGE = "normal_exchange";
    //死信交换机名称
    private static final String DEAD_EXCHANGE = "dead_exchange";
    //普通队列
    public static final String NORMAL_QUEUE = "normal_queue";
    //死信队列
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws Exception {
        //创建信道
        Channel channel = RabbitMqUtils.getChannel();
        //声明死信和普通交换机
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);
        //声明普通队列
        HashMap<String, Object> arguments = new HashMap<>();
        //队列最大长度 6
        arguments.put("x-max-length",6);
        //正常队列死信交换机
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        //设置死信routingkey
        arguments.put("x-dead-letter-routing-key","lisi");

        channel.queueDeclare(NORMAL_QUEUE, false, false, false, arguments);

        //============================================================
        //声明死信队列
        channel.queueDeclare(DEAD_QUEUE, false, false, false, null);

        //绑定普通队列交换机
        channel.queueBind(NORMAL_QUEUE, NORMAL_EXCHANGE, "zhangsan");
        //绑定死信队列和交换机
        channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, "lisi");

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("01接收消息" + new String(message.getBody()));
        };
        CancelCallback cancelCallback = consumerTag -> {};

        channel.basicConsume(NORMAL_QUEUE, true, deliverCallback,cancelCallback);
    }
}
