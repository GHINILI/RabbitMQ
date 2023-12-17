package com.lwj.mq02_workqueues;

import com.lwj.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.util.concurrent.TimeUnit;

public class Task01 {

    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //队列声明
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        for (int i = 0; i < 20; i++) {

            String message = "第 "+ i + " 条消息";
            //发消息
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
            TimeUnit.MILLISECONDS.sleep(30);
        }
    }
}
