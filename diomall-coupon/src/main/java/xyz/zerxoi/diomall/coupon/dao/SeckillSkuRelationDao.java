package xyz.zerxoi.diomall.coupon.dao;

import xyz.zerxoi.diomall.coupon.entity.SeckillSkuRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 秒杀活动商品关联
 * 
 * @author zerxoi
 * @email zerxoi1997@gmail.com
 * @date 2021-08-11 23:25:19
 */
@Mapper
public interface SeckillSkuRelationDao extends BaseMapper<SeckillSkuRelationEntity> {
	
}
