package xyz.zerxoi.diomall.product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import xyz.zerxoi.common.utils.PageUtils;
import xyz.zerxoi.common.utils.R;
import xyz.zerxoi.diomall.product.entity.CategoryEntity;
import xyz.zerxoi.diomall.product.service.CategoryService;

import java.util.Arrays;
import java.util.Map;


/**
 * 商品三级分类
 *
 * @author zerxoi
 * @email zerxoi1997@gmail.com
 * @date 2021-08-11 01:21:29
 */
@RestController
@RequestMapping("product/category")
public class CategoryController {
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    private CategoryService categoryService;

    @RequestMapping("/list/tree")
    public R listTree() {
        // ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        // String categoryTree = ops.get("category_tree");
        // List<CategoryEntity> categoryEntities;
        // if (StringUtils.isNotEmpty(categoryTree)) {
        //     categoryEntities = JSON.parseObject(categoryTree, new TypeReference<List<CategoryEntity>>() {
        //     });
        // } else {
        //     categoryEntities = categoryService.listTree();
        //     String s = JSON.toJSONString(categoryEntities);
        //     ops.set("category_tree", s);
        // }

        // 使用 Spring Cache 简化上述过程
        return R.ok().put("data", categoryService.listTree());
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = categoryService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 批量修改
     */
    @RequestMapping("/update/batch")
    public R updateBatch(@RequestBody CategoryEntity[] categories) {
        categoryService.updateBatchByIdEvict(categories);

        return R.ok();
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{catId}")
    public R info(@PathVariable("catId") Long catId) {
        CategoryEntity category = categoryService.getById(catId);

        return R.ok().put("category", category);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody CategoryEntity category) {
        categoryService.saveEvict(category);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody CategoryEntity category) {
        categoryService.updateCascade(category);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] catIds) {
        categoryService.removeCascade(Arrays.asList(catIds));

        return R.ok();
    }

}
