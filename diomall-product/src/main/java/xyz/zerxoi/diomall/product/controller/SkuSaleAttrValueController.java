package xyz.zerxoi.diomall.product.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.zerxoi.common.utils.PageUtils;
import xyz.zerxoi.common.utils.R;
import xyz.zerxoi.diomall.product.entity.SkuSaleAttrValueEntity;
import xyz.zerxoi.diomall.product.service.SkuSaleAttrValueService;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * sku销售属性&值
 *
 * @author zerxoi
 * @email zerxoi1997@gmail.com
 * @date 2021-08-11 01:21:29
 */
@RestController
@RequestMapping("product/skusaleattrvalue")
public class SkuSaleAttrValueController {
    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = skuSaleAttrValueService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        SkuSaleAttrValueEntity skuSaleAttrValue = skuSaleAttrValueService.getById(id);

        return R.ok().put("skuSaleAttrValue", skuSaleAttrValue);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody SkuSaleAttrValueEntity skuSaleAttrValue) {
        skuSaleAttrValueService.save(skuSaleAttrValue);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody SkuSaleAttrValueEntity skuSaleAttrValue) {
        skuSaleAttrValueService.updateById(skuSaleAttrValue);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        skuSaleAttrValueService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    @GetMapping("/stringlist/{skuId}")
    public List<String> getSkuSaleAttrValue(@PathVariable("skuId") Long skuId) {
        List<SkuSaleAttrValueEntity> list =
                skuSaleAttrValueService.list(new LambdaQueryWrapper<SkuSaleAttrValueEntity>().eq(SkuSaleAttrValueEntity::getSkuId, skuId));
        return list.stream().map(entity -> entity.getAttrName() + ":" + entity.getAttrValue()).collect(Collectors.toList());
    }
}
