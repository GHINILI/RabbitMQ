package com.lwj.mq06;

import com.lwj.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

public class ReceiveLogsDirect02 {

    public static final String EXCHANGE_NAME = "direct_logs";
    public static final String QUEUE_NAME_DISK = "disk";
    public static final String ERROR_TYPE = "error";

    public static void main(String[] args) throws Exception{
        //获取信道
        Channel channel = RabbitMqUtils.getChannel();
        //声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        //声明队列
        channel.queueDeclare(QUEUE_NAME_DISK, false, false, false, null);
        //绑定
        channel.queueBind(QUEUE_NAME_DISK, EXCHANGE_NAME, ERROR_TYPE);

        //接收消息
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("ReceiveLogsDirect02控制台打印收到的消息：" + new String(message.getBody(), "UTF-8"));
        };
        //接收中断回调
        CancelCallback cancelCallback = consumerTag -> {};
        channel.basicConsume("console", true, deliverCallback, cancelCallback);

    }
}
