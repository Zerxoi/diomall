package xyz.zerxoi.diomall.ware.dao;

import org.apache.ibatis.annotations.Param;
import xyz.zerxoi.diomall.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * εεεΊε­
 *
 * @author zerxoi
 * @email zerxoi1997@gmail.com
 * @date 2021-08-12 00:18:34
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {
    void addStock(@Param("skuId") Long skuId, @Param("wareId") Long wareId, @Param("skuNum") Integer skuNum);
}
