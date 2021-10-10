package xyz.zerxoi.diomall.product.controller;

import java.util.Arrays;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import xyz.zerxoi.common.utils.PageUtils;
import xyz.zerxoi.common.utils.Query;
import xyz.zerxoi.common.utils.R;
import xyz.zerxoi.diomall.product.entity.AttrGroupEntity;
import xyz.zerxoi.diomall.product.service.AttrGroupService;


/**
 * 属性分组
 *
 * @author zerxoi
 * @email zerxoi1997@gmail.com
 * @date 2021-08-11 01:21:29
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;

    @RequestMapping("/list/{catId}")
    public R listByCatId(@RequestParam Map<String, Object> params, @PathVariable("catId") Long catId) {
        String key = (String) params.get("key");
        IPage<AttrGroupEntity> page = attrGroupService.page(new Query<AttrGroupEntity>().getPage(params),
                new LambdaQueryWrapper<AttrGroupEntity>()
                        .eq(catId != null && catId > 0, AttrGroupEntity::getCatelogId, catId)
                        .and(StringUtils.isNotBlank(key), w -> w.eq(AttrGroupEntity::getAttrGroupId, key).or()
                                .like(AttrGroupEntity::getAttrGroupName, key)));
        return R.ok().put("page", new PageUtils(page));
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    public R info(@PathVariable("attrGroupId") Long attrGroupId) {
        AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);

        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrGroupEntity attrGroup) {
        attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrGroupEntity attrGroup) {
        attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrGroupIds) {
        attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

}
