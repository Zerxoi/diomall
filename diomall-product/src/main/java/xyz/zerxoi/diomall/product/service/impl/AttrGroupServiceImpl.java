package xyz.zerxoi.diomall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.zerxoi.common.utils.PageUtils;
import xyz.zerxoi.common.utils.Query;
import xyz.zerxoi.diomall.product.dao.AttrGroupDao;
import xyz.zerxoi.diomall.product.entity.AttrAttrgroupRelationEntity;
import xyz.zerxoi.diomall.product.entity.AttrEntity;
import xyz.zerxoi.diomall.product.entity.AttrGroupEntity;
import xyz.zerxoi.diomall.product.service.AttrAttrgroupRelationService;
import xyz.zerxoi.diomall.product.service.AttrGroupService;
import xyz.zerxoi.diomall.product.service.AttrService;
import xyz.zerxoi.diomall.product.service.CategoryService;
import xyz.zerxoi.diomall.product.vo.AttrGroupVo;
import xyz.zerxoi.diomall.product.vo.AttrGroupWithAttrsVo;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {
    @Autowired
    private AttrAttrgroupRelationService attrAttrgroupRelationService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private AttrService attrService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional
    public void removeCascade(List<Long> attrGroupIds) {
        removeByIds(attrGroupIds);
        attrAttrgroupRelationService.remove(new LambdaQueryWrapper<AttrAttrgroupRelationEntity>()
                .in(AttrAttrgroupRelationEntity::getAttrGroupId, attrGroupIds));
    }

    @Override
    public AttrGroupVo getAttrGroupInfo(Long attrGroupId) {
        AttrGroupVo attrGroupVo = new AttrGroupVo();
        AttrGroupEntity attrGroupEntity = getById(attrGroupId);
        BeanUtils.copyProperties(attrGroupEntity, attrGroupVo);
        Long[] catelogPath = categoryService.findCatelogPath(attrGroupEntity.getCatelogId());
        attrGroupVo.setCatelogPath(catelogPath);
        return attrGroupVo;
    }

    @Override
    public List<AttrGroupWithAttrsVo> getAttrGroupWithAttrsByCatelogId(Long catelogId) {
        // 1. 查出当前分类下的所有属性分组
        List<AttrGroupEntity> attrGroupEntities = this.list(new LambdaQueryWrapper<AttrGroupEntity>()
                .eq(AttrGroupEntity::getCatelogId, catelogId));

        // 2. 查出每个属性分组下的所有属性
        return attrGroupEntities.stream().map(attrGroupEntity -> {
            AttrGroupWithAttrsVo attrGroupWithAttrsVo = new AttrGroupWithAttrsVo();
            BeanUtils.copyProperties(attrGroupEntity, attrGroupWithAttrsVo);
            List<AttrEntity> attrs = attrService.getRelationAttr(attrGroupWithAttrsVo.getAttrGroupId());
            attrGroupWithAttrsVo.setAttrs(attrs);
            return attrGroupWithAttrsVo;
        }).collect(Collectors.toList());
    }

}