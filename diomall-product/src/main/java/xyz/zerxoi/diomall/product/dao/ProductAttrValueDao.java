package xyz.zerxoi.diomall.product.dao;

import xyz.zerxoi.diomall.product.entity.ProductAttrValueEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * spu属性值
 * 
 * @author chenshun
 * @email zerxoi1997@gmail.com
 * @date 2021-08-11 00:44:37
 */
@Mapper
public interface ProductAttrValueDao extends BaseMapper<ProductAttrValueEntity> {
	
}
