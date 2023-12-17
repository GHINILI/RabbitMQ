package com.lwj.mq03;

import com.lwj.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

//消息手动应答测试，生产者
public class Task02 {

    //队列名称
    public static final String TASK_QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMqUtils.getChannel();

        //声明队列
        channel.queueDeclare(TASK_QUEUE_NAME,false,false,false,null);

        for (int i = 1; i <= 20; i++) {

            String message = "第 "+ i + " 条消息";
            //发消息
            channel.basicPublish("",TASK_QUEUE_NAME,null,message.getBytes());
        }
    }
}
