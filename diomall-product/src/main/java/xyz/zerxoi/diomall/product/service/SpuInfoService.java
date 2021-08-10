package xyz.zerxoi.diomall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.zerxoi.common.utils.PageUtils;
import xyz.zerxoi.diomall.product.entity.SpuInfoEntity;

import java.util.Map;

/**
 * spu信息
 *
 * @author chenshun
 * @email zerxoi1997@gmail.com
 * @date 2021-08-11 00:44:37
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

