package xyz.zerxoi.diomall.coupon.dao;

import xyz.zerxoi.diomall.coupon.entity.SkuLadderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品阶梯价格
 * 
 * @author zerxoi
 * @email zerxoi1997@gmail.com
 * @date 2021-08-11 23:25:19
 */
@Mapper
public interface SkuLadderDao extends BaseMapper<SkuLadderEntity> {
	
}
