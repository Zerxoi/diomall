package xyz.zerxoi.diomall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.zerxoi.common.utils.PageUtils;
import xyz.zerxoi.common.utils.Query;
import xyz.zerxoi.diomall.product.dao.CategoryDao;
import xyz.zerxoi.diomall.product.entity.CategoryBrandRelationEntity;
import xyz.zerxoi.diomall.product.entity.CategoryEntity;
import xyz.zerxoi.diomall.product.service.CategoryBrandRelationService;
import xyz.zerxoi.diomall.product.service.CategoryService;

import java.util.*;
import java.util.stream.Collectors;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    // sync = true 添加本地锁 预防缓存击穿
    @Cacheable(cacheNames = "category", key = "'tree'", sync = true)
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

    @Override
    // 清空 category 缓存名中的所有 key
    @CacheEvict(cacheNames = "category", key = "'tree'", allEntries = true)
    public void updateBatchByIdEvict(CategoryEntity[] categories) {
        this.updateBatchById(Arrays.asList(categories));
    }

    @Override
    @CacheEvict(cacheNames = "category", key = "'tree'", allEntries = true)
    public void saveEvict(CategoryEntity category) {
        save(category);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "category", key = "'tree'", allEntries = true)
    public void updateCascade(CategoryEntity category) {
        updateById(category);
        categoryBrandRelationService.updateCategory(category.getCatId(), category.getName());
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "category", key = "'tree'", allEntries = true)
    public void removeCascade(List<Long> catIds) {
        removeByIds(catIds);
        categoryBrandRelationService.remove(new LambdaQueryWrapper<CategoryBrandRelationEntity>()
                .in(CategoryBrandRelationEntity::getCatelogId, catIds));
    }


    @Override
    public Long[] findCatelogPath(Long catelogId) {
        List<Long> paths = new ArrayList<>();
        List<Long> parentPath = findParentPath(catelogId, paths);
        Collections.reverse(parentPath);
        return parentPath.toArray(new Long[0]);
    }

    private List<Long> findParentPath(Long catelogId, List<Long> paths) {
        // 1. 搜集当前节点Id
        paths.add(catelogId);
        CategoryEntity byId = getById(catelogId);
        if (byId.getParentCid() != 0) {
            findParentPath(byId.getParentCid(), paths);
        }
        return paths;
    }
}