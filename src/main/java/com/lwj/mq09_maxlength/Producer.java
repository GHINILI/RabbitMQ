package com.lwj.mq09_maxlength;

import com.lwj.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.util.concurrent.TimeUnit;

/**
 *死信队列
 * 生产者
 * 模拟队列到达最长
 */
public class Producer {
    //普通交换机
    public static final String NORMAL_EXCHANGE = "normal_exchange";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        for (int i = 1; i <11; i++) {
            String message = "info: " + i;
            channel.basicPublish(NORMAL_EXCHANGE, "zhangsan", null, message.getBytes());
            TimeUnit.MILLISECONDS.sleep(1000);
        }
    }
}
