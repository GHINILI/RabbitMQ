package com.lwj.mq07;

import com.lwj.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.util.HashMap;
import java.util.Map;

/**
 * 生产者
 *
 */
public class EmitLogTopic {
    public static final String EXCHANGE_NAME = "topic_logs";
    public static final String EXCHANGE_TYPE = "topic";

    public static void main(String[] args) throws Exception {
        //获得信道
        Channel channel = RabbitMqUtils.getChannel();
        //声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE);

        HashMap<String, String> bindingKey = new HashMap<>();
        bindingKey.put("quick.orange.rabbit","被队列 Q1Q2 接收到");
        bindingKey.put("lazy.orange.elephant","被队列 Q1Q2 接收到");
        bindingKey.put("quick.orange.fox","被队列 Q1 接收到");
        bindingKey.put("lazy.brown.fox","被队列 Q2 接收到");
        bindingKey.put("lazy.pink.rabbit","虽然满足两个绑定但只被队列 Q2 接收一次");
        bindingKey.put("quick.brown.fox","不匹配任何绑定不会被任何队列接收到会被丢弃");
        bindingKey.put("quick.orange.male.rabbit","是四个单词不匹配任何绑定会被丢弃");
        bindingKey.put("lazy.orange.male.rabbit","是四个单词但匹配 Q2");

        for (Map.Entry<String, String> bindingKeyEntry : bindingKey.entrySet()) {
            String routingKey = bindingKeyEntry.getKey();
            String message = bindingKeyEntry.getValue();
            channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
            System.out.println("生产者发送消息：" + message);
        }
    }
}
