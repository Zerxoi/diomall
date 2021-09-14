package xyz.zerxoi.diomall.product.service.impl;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import xyz.zerxoi.common.utils.PageUtils;
import xyz.zerxoi.common.utils.Query;
import xyz.zerxoi.diomall.product.dao.CategoryDao;
import xyz.zerxoi.diomall.product.entity.CategoryEntity;
import xyz.zerxoi.diomall.product.service.CategoryService;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listTree() {
        List<CategoryEntity> categoryEntities = baseMapper.selectList(null);
        return categoryEntities.stream().filter(categoryEntity -> categoryEntity.getParentCid() == 0)
                .peek(entity -> entity.setChildren(getChildren(entity, categoryEntities)))
                .sorted(Comparator.comparingInt(o -> (o.getSort() == null ? 0 : o.getSort())))
                .collect(Collectors.toList());
    }

    private List<CategoryEntity> getChildren(CategoryEntity parent, List<CategoryEntity> all) {
        return all.stream().filter(entity -> entity.getParentCid().equals(parent.getCatId()))
                .peek(entity -> entity.setChildren(getChildren(entity, all)))
                .sorted(Comparator.comparingInt(o -> (o.getSort() == null ? 0 : o.getSort())))
                .collect(Collectors.toList());
    }

}