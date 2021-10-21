package xyz.zerxoi.diomall.search.vo;

import java.util.List;

import lombok.Data;

/**
 * @author zouxin <zouxin@kuaishou.com>
 * Created on 2021-10-12
 */
@Data
public class SearchParam {
    private String keyword;
    private Long catalog3Id;
    // sort { field, asc }
    private String sort;

    private Integer hasStock;
    private String skuPrice;
    private List<Long> brandId;
    private List<String> attrs;
    private Integer pageNum;

}
