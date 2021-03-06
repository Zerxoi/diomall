package xyz.zerxoi.diomall.order.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import xyz.zerxoi.diomall.order.entity.OrderEntity;
import xyz.zerxoi.diomall.order.entity.OrderOperateHistoryEntity;
import xyz.zerxoi.diomall.order.service.OrderService;
import xyz.zerxoi.common.utils.PageUtils;
import xyz.zerxoi.common.utils.R;



/**
 * 订单
 *
 * @author zerxoi
 * @email zerxoi1997@gmail.com
 * @date 2021-08-11 23:40:21
 */
@RefreshScope
@RestController
@RequestMapping("order/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${diomall.user.name}")
    private String name;
    @Value("${diomall.user.age}")
    private Integer age;

    @RequestMapping("/config")
    public R config() {
        return R.ok().put("name", name).put("age", age);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
        public R list(@RequestParam Map<String, Object> params){
        PageUtils page = orderService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
        public R info(@PathVariable("id") Long id){
		OrderEntity order = orderService.getById(id);

        return R.ok().put("order", order);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
        public R save(@RequestBody OrderEntity order){
		orderService.save(order);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
        public R update(@RequestBody OrderEntity order){
		orderService.updateById(order);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
        public R delete(@RequestBody Long[] ids){
		orderService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    @RequestMapping("/sendMessage")
    public R sendMessage(){
        for (int i = 0; i < 10; i++) {
            OrderOperateHistoryEntity historyEntity = new OrderOperateHistoryEntity();
            historyEntity.setOrderId(1L);
            historyEntity.setCreateTime(new Date());
            historyEntity.setOrderStatus(0);
            historyEntity.setOperateMan("Jinx");
            // 如果发送消息是个对象，对象通过序列化机制发送对象
            rabbitTemplate.convertAndSend("java.exchange", "hello.java", historyEntity,
                    new CorrelationData(UUID.randomUUID().toString()));
        }
        return R.ok("成功");
    }

}
