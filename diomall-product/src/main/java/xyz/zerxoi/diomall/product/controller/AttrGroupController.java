package xyz.zerxoi.diomall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import xyz.zerxoi.common.utils.PageUtils;
import xyz.zerxoi.common.utils.Query;
import xyz.zerxoi.common.utils.R;
import xyz.zerxoi.diomall.product.entity.AttrEntity;
import xyz.zerxoi.diomall.product.entity.AttrGroupEntity;
import xyz.zerxoi.diomall.product.service.AttrAttrgroupRelationService;
import xyz.zerxoi.diomall.product.service.AttrGroupService;
import xyz.zerxoi.diomall.product.service.AttrService;
import xyz.zerxoi.diomall.product.vo.AttrGroupRelationVo;
import xyz.zerxoi.diomall.product.vo.AttrGroupVo;
import xyz.zerxoi.diomall.product.vo.AttrGroupWithAttrsVo;


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
    @Autowired
    private AttrService attrService;
    @Autowired
    private AttrAttrgroupRelationService relationService;

    @GetMapping("/{attrgroupId}/attr/relation")
    public R attrRelation(@PathVariable("attrgroupId") Long attrGroupId){
        List<AttrEntity> entities =  attrService.getRelationAttr(attrGroupId);
        return R.ok().put("data",entities);
    }

    @GetMapping("/{attrgroupId}/noattr/relation")
    public R attrNoRelation(@PathVariable("attrgroupId") Long attrGroupId,
                            @RequestParam Map<String, Object> params){
        PageUtils page = attrService.getNoRelationAttr(params,attrGroupId);
        return R.ok().put("page",page);
    }

    @PostMapping("/attr/relation")
    public R addRelation(@RequestBody List<AttrGroupRelationVo> vos){
        relationService.saveBatch(vos);

        return R.ok();
    }

    @PostMapping("/attr/relation/delete")
    public R deleteRelation(@RequestBody  AttrGroupRelationVo[] vos){
        attrService.deleteRelation(vos);

        return R.ok();
    }

    @GetMapping("/{catelogId}/withattr")
    public R getAttrGroupWithAttrs(@PathVariable("catelogId")Long catelogId){

        //1、查出当前分类下的所有属性分组，
        //2、查出每个属性分组的所有属性
        List<AttrGroupWithAttrsVo> vos =  attrGroupService.getAttrGroupWithAttrsByCatelogId(catelogId);
        return R.ok().put("data",vos);
    }


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
        AttrGroupVo attrGroup = attrGroupService.getAttrGroupInfo(attrGroupId);

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
        attrGroupService.removeCascade(Arrays.asList(attrGroupIds));

        return R.ok();
    }

}
