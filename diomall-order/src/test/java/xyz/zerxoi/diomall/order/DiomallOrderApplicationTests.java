package xyz.zerxoi.diomall.order;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.zerxoi.diomall.order.entity.OrderOperateHistoryEntity;
import xyz.zerxoi.diomall.order.entity.OrderReturnReasonEntity;

import java.util.Date;
import java.util.UUID;

@Slf4j
@SpringBootTest
class DiomallOrderApplicationTests {

    @Autowired
    private AmqpAdmin amqpAdmin;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    void createExchange() {
        amqpAdmin.declareExchange(new DirectExchange("java.exchange", true, false));
        log.info("Exchange创建成功");
    }


    @Test
    void createQueue() {
        amqpAdmin.declareQueue(new Queue("java-queue", true, false, false));
        log.info("Queue创建成功");
    }

    @Test
    void createBinding() {
        amqpAdmin.declareBinding(new Binding("java-queue", Binding.DestinationType.QUEUE, "java.exchange", "hello" +
                ".java", null));
        log.info("Binding创建成功");
    }

    @Test
    void sendOperateHistory() {
        OrderOperateHistoryEntity historyEntity = new OrderOperateHistoryEntity();
        historyEntity.setOrderId(1L);
        historyEntity.setCreateTime(new Date());
        historyEntity.setOrderStatus(0);
        historyEntity.setOperateMan("Jinx");
        // 如果发送消息是个对象，对象通过序列化机制发送对象
        rabbitTemplate.convertAndSend("java.exchange", "hello.java", historyEntity,
                new CorrelationData(UUID.randomUUID().toString()));
    }

    @Test
    void sendReturnReason() {
        OrderReturnReasonEntity returnReason = new OrderReturnReasonEntity();
        returnReason.setCreateTime(new Date());
        returnReason.setId(1L);
        returnReason.setName("Violate");
        returnReason.setStatus(0);
        rabbitTemplate.convertAndSend("java.exchange", "hello.java", returnReason,
                new CorrelationData(UUID.randomUUID().toString()));
    }

    @Test
    void sendMessage() {
        for (int i = 0; i < 10; i++) {
            // if (i % 2 == 0) {
                OrderOperateHistoryEntity historyEntity = new OrderOperateHistoryEntity();
                historyEntity.setOrderId(1L);
                historyEntity.setCreateTime(new Date());
                historyEntity.setOrderStatus(0);
                historyEntity.setOperateMan("Jinx");
                // 如果发送消息是个对象，对象通过序列化机制发送对象
                rabbitTemplate.convertAndSend("java.exchange", "hello.java", historyEntity,
                        new CorrelationData(UUID.randomUUID().toString()));
            // } else {
            //     OrderReturnReasonEntity returnReason = new OrderReturnReasonEntity();
            //     returnReason.setCreateTime(new Date());
            //     returnReason.setId(1L);
            //     returnReason.setName("Violate");
            //     returnReason.setStatus(0);
            //     rabbitTemplate.convertAndSend("java.exchange", "hello.java", returnReason,
            //             new CorrelationData(UUID.randomUUID().toString()));
            // }
        }
    }
}
