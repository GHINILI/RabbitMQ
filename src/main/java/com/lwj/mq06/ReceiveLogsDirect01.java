package com.lwj.mq06;

import com.lwj.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

public class ReceiveLogsDirect01 {

    public static final String EXCHANGE_NAME = "direct_logs";
    public static final String QUEUE_NAME_CONSOLE = "console";
    public static final String INFO_TYPE = "info";
    public static final String WARNING_TYPE = "warning";

    public static void main(String[] args) throws Exception{
        //获取信道
        Channel channel = RabbitMqUtils.getChannel();
        //声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        //声明队列
        channel.queueDeclare(QUEUE_NAME_CONSOLE, false, false, false, null);
        //绑定
        channel.queueBind(QUEUE_NAME_CONSOLE, EXCHANGE_NAME, INFO_TYPE);
        channel.queueBind(QUEUE_NAME_CONSOLE, EXCHANGE_NAME, WARNING_TYPE);
        //接收消息
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("ReceiveLogsDirect01控制台打印收到的消息：" + new String(message.getBody(), "UTF-8"));
        };
        //接收中断回调
        CancelCallback cancelCallback = consumerTag -> {};
        channel.basicConsume("console", true, deliverCallback, cancelCallback);

    }
}
