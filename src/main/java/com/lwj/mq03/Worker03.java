package com.lwj.mq03;

import com.lwj.utils.RabbitMqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.util.concurrent.TimeUnit;

//消息手动应答，数据不丢失
public class Worker03 {

    public static final String TASK_QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            try { TimeUnit.MILLISECONDS.sleep(1000L);} catch (InterruptedException e) {throw new RuntimeException(e);}
            System.out.println("每隔一秒接收到的消息： " + new String(message.getBody()));
            //手动应答,不批量应答
            channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
        };

        CancelCallback cancelCallback = consumerTag -> {
            System.out.println("03消息消费中断");
        };
        //手动应答
        boolean autoAck = false;
        channel.basicConsume(TASK_QUEUE_NAME,autoAck,deliverCallback, cancelCallback);
    }
}
