package com.lwj.mq04;

import com.lwj.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;

import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class ConfirmMessage {
    public static final int MESSAGE_COUNT = 1000;
    public static void main(String[] args) throws Exception {
        publishMessageAsync();//发布1000异步确认发布耗时：72ms

    }

    //异步发布确认
    public static void publishMessageAsync() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //队列声明
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true,false, false, null);
        //开启发布确认
        channel.confirmSelect();
        /**
         * 线程安全有序的哈希表，适用于高并发下
         * 1. 轻松将序号和消息关联
         * 2. 轻松删除
         * 3. 多线程
         */
        ConcurrentSkipListMap<Long, String> outstandingConfirm = new ConcurrentSkipListMap<>();
        //开始时间
        long begin = System.currentTimeMillis();

        //核心代码
        //准备监听器，监听发布
        /**成功回调函数
         * 1.消息标识
         * 2.是否批量确认
         */
        ConfirmCallback ackCallback = (deliveryTag, multiple) ->{
            //第二步，删除已经确认发布的消息
            if (multiple) {
                ConcurrentNavigableMap<Long, String> confirmed =
                        outstandingConfirm.headMap(deliveryTag);
                confirmed.clear();
            }else {
                outstandingConfirm.remove(deliveryTag);
            }
            System.out.println("确认消息: " + deliveryTag);
        };
        /**失败回调函数
         * 1.消息标识
         * 2.是否批量确认
         */
        ConfirmCallback nackCallback = (deliveryTag, multiple) ->{
            String message = outstandingConfirm.get(deliveryTag);
            System.out.println("未确认消息: " + message + "/t未确认消息Tag: " + deliveryTag);
        };
        channel.addConfirmListener(ackCallback,nackCallback);//异步确认
        //批量发布消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message ="第 " + (i + 1) + " 消息";
            channel.basicPublish("", queueName, null, message.getBytes());
            //第一步，此处记录要发布的消息
            outstandingConfirm.put(channel.getNextPublishSeqNo(),message);
        }

        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "异步确认发布耗时：" + (end - begin) + "ms");
    }

}

