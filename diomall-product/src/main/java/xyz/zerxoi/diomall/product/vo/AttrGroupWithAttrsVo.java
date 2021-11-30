package xyz.zerxoi.diomall.product.vo;

import lombok.Data;
import xyz.zerxoi.diomall.product.entity.AttrEntity;

import java.util.List;

@Data
public class AttrGroupWithAttrsVo {
    private Long attrGroupId;
    /**
     * 组名
     */
    private String attrGroupName;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 描述
     */
    private String descript;
    /**
     * 组图标
     */
    private String icon;
    /**
     * 所属分类id
     */
    private Long catelogId;

    /**
     * 所有关联属性
     */
    private List<AttrEntity> attrs;
}