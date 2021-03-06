package xyz.zerxoi.diomall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.zerxoi.common.utils.PageUtils;
import xyz.zerxoi.diomall.product.entity.BrandEntity;

import java.util.List;
import java.util.Map;

/**
 * 品牌
 *
 * @author zerxoi
 * @email zerxoi1997@gmail.com
 * @date 2021-08-11 00:44:37
 */
public interface BrandService extends IService<BrandEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void updateCascade(BrandEntity brand);

    void removeCascade(List<Long> brandIds);
}

