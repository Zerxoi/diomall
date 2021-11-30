package xyz.zerxoi.diomall.product.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xyz.zerxoi.diomall.product.entity.AttrGroupEntity;

@Data
public class AttrGroupVo extends AttrGroupEntity {
    private Long[] catelogPath;
}
