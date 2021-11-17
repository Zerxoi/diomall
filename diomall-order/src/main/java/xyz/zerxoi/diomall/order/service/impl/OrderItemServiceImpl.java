package xyz.zerxoi.diomall.order.service.impl;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.zerxoi.common.utils.PageUtils;
import xyz.zerxoi.common.utils.Query;

import xyz.zerxoi.diomall.order.dao.OrderItemDao;
import xyz.zerxoi.diomall.order.entity.OrderItemEntity;
import xyz.zerxoi.diomall.order.entity.OrderOperateHistoryEntity;
import xyz.zerxoi.diomall.order.entity.OrderReturnReasonEntity;
import xyz.zerxoi.diomall.order.service.OrderItemService;

@Service("orderItemService")
@RabbitListener(queues = {"java-queue"})
public class OrderItemServiceImpl extends ServiceImpl<OrderItemDao, OrderItemEntity> implements OrderItemService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderItemEntity> page = this.page(
                new Query<OrderItemEntity>().getPage(params),
                new QueryWrapper<OrderItemEntity>()
        );

        return new PageUtils(page);
    }

    @RabbitHandler
    public void receiveOrderOperateHistory(Message message, OrderOperateHistoryEntity body, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        if ((deliveryTag & 1) == 0) {
            // 非批量Ack响应
            System.out.println("响应：" + message);
            channel.basicAck(deliveryTag, false);
        }
        else {
            System.out.println("拒绝：" + message);
            channel.basicNack(deliveryTag, false, false);
        }
    }

    @RabbitHandler
    public void receiveOrderReturnReason(OrderReturnReasonEntity body) {
        System.out.println("订单退回原因");
        System.out.println(body);
    }

}