package xyz.zerxoi.diomall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import xyz.zerxoi.common.utils.PageUtils;
import xyz.zerxoi.common.utils.R;
import xyz.zerxoi.diomall.product.entity.CategoryEntity;
import xyz.zerxoi.diomall.product.service.CategoryService;


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
        return R.ok().put("data", categoryService.listTree());
    }

    @RequestMapping("/update/tree")
    public R updateTree() {
        categoryService.updateTree();
        return R.ok();
    }

    @RequestMapping("/list/tree/cache")
    public R listTreeCache() {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        String categoryTree = ops.get("category_tree");
        List<CategoryEntity> categoryEntities;
        if (StringUtils.isNotEmpty(categoryTree)) {
            categoryEntities = JSON.parseObject(categoryTree, new TypeReference<List<CategoryEntity>>() {
            });
        } else {
            categoryEntities = categoryService.listTree();
            String s = JSON.toJSONString(categoryEntities);
            ops.set("category_tree", s);
        }
        return R.ok().put("data", categoryEntities);
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
        categoryService.save(category);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody CategoryEntity category) {
        categoryService.updateById(category);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] catIds) {
        categoryService.removeByIds(Arrays.asList(catIds));

        return R.ok();
    }

}
