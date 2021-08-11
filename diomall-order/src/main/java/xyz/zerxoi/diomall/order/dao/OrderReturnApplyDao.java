package xyz.zerxoi.diomall.order.dao;

import xyz.zerxoi.diomall.order.entity.OrderReturnApplyEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单退货申请
 * 
 * @author zerxoi
 * @email zerxoi1997@gmail.com
 * @date 2021-08-11 23:40:21
 */
@Mapper
public interface OrderReturnApplyDao extends BaseMapper<OrderReturnApplyEntity> {
	
}
