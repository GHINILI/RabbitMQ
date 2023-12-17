package com.lwj.mq10_reject;

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
            String ms = new String(message.getBody());
            if (ms.equals("info: 7")){
                System.out.println("*01拒收消息：" + ms);
                //获得标签,不放回队列
                channel.basicReject(message.getEnvelope().getDeliveryTag(),false);
            }else {
                System.out.println("01接收消息：" + ms);
                channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
            }
        };
        CancelCallback cancelCallback = consumerTag -> {};
        //开启手动应答
        channel.basicConsume(NORMAL_QUEUE, false, deliverCallback,cancelCallback);
    }
}
