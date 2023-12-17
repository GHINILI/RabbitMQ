package com.lwj.mq08_ttl;

import com.lwj.utils.RabbitMqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;

import java.util.concurrent.TimeUnit;

/**
 *死信队列
 * 生产者
 * 模拟ttl到期
 */
public class Producer {
    //普通交换机
    public static final String NORMAL_EXCHANGE = "normal_exchange";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //死信消息
        AMQP.BasicProperties properties =
                new AMQP.BasicProperties()
                        .builder().expiration("10000").build();
        for (int i = 1; i <11; i++) {
            String message = "info: " + i;
            channel.basicPublish(NORMAL_EXCHANGE, "zhangsan", properties, message.getBytes());
            TimeUnit.MILLISECONDS.sleep(1000);
        }
    }
}
