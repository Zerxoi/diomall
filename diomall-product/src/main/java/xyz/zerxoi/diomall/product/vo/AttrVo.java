package xyz.zerxoi.diomall.product.vo;

import lombok.Data;
import xyz.zerxoi.diomall.product.entity.AttrEntity;

@Data
public class AttrVo extends AttrEntity {
    private Long attrGroupId;
    private String catelogName;
    private String groupName;
    private Long[] catelogPath;
}