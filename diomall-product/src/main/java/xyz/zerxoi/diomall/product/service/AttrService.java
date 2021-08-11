package xyz.zerxoi.diomall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.zerxoi.common.utils.PageUtils;
import xyz.zerxoi.diomall.product.entity.AttrEntity;

import java.util.Map;

/**
 * 商品属性
 *
 * @author zerxoi
 * @email zerxoi1997@gmail.com
 * @date 2021-08-11 00:44:36
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

