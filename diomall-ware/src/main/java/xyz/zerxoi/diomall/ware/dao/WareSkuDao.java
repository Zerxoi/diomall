package xyz.zerxoi.diomall.ware.dao;

import xyz.zerxoi.diomall.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品库存
 * 
 * @author zerxoi
 * @email zerxoi1997@gmail.com
 * @date 2021-08-12 00:18:34
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {
	
}
