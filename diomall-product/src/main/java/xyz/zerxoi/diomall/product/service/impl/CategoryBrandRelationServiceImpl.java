package xyz.zerxoi.diomall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.zerxoi.common.utils.PageUtils;
import xyz.zerxoi.common.utils.Query;
import xyz.zerxoi.diomall.product.dao.CategoryBrandRelationDao;
import xyz.zerxoi.diomall.product.entity.BrandEntity;
import xyz.zerxoi.diomall.product.entity.CategoryBrandRelationEntity;
import xyz.zerxoi.diomall.product.entity.CategoryEntity;
import xyz.zerxoi.diomall.product.service.BrandService;
import xyz.zerxoi.diomall.product.service.CategoryBrandRelationService;
import xyz.zerxoi.diomall.product.service.CategoryService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao,
        CategoryBrandRelationEntity> implements CategoryBrandRelationService {
    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private CategoryService categoryService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<BrandEntity> getBrandsByCatId(Long catId) {
        List<CategoryBrandRelationEntity> categoryBrandRelationEntities =
                categoryBrandRelationService.list(new LambdaQueryWrapper<CategoryBrandRelationEntity>()
                        .eq(CategoryBrandRelationEntity::getCatelogId, catId));
        return categoryBrandRelationEntities.stream().map(item -> brandService.getById(item.getBrandId()))
                .collect(Collectors.toList());
    }

    @Override
    public void saveDetail(CategoryBrandRelationEntity categoryBrandRelation) {
        Long brandId = categoryBrandRelation.getBrandId();
        Long catelogId = categoryBrandRelation.getCatelogId();

        // 查询详细名称
        CategoryEntity categoryEntity = categoryService.getById(catelogId);
        BrandEntity brandEntity = brandService.getById(brandId);

        categoryBrandRelation.setBrandName(brandEntity.getName());
        categoryBrandRelation.setCatelogName(categoryEntity.getName());

        save(categoryBrandRelation);
    }

    @Override
    public void updateCategory(Long catId, String name) {
        update(new LambdaUpdateWrapper<CategoryBrandRelationEntity>()
                .set(CategoryBrandRelationEntity::getCatelogName, name)
                .eq(CategoryBrandRelationEntity::getCatelogId, catId));
    }

    @Override
    public void updateBrand(Long brandId, String name) {
        update(new LambdaUpdateWrapper<CategoryBrandRelationEntity>()
                .set(CategoryBrandRelationEntity::getBrandName, name)
                .eq(CategoryBrandRelationEntity::getBrandId, brandId));
    }
}