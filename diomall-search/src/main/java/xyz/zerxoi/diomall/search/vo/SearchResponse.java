package xyz.zerxoi.diomall.search.vo;

import java.util.List;

/**
 * @author zouxin <zouxin@kuaishou.com>
 * Created on 2021-10-12
 */
public class SearchResponse {
    private List<Object> products;

    private Integer pageNum;
    private Long total;
    private Integer pages;

    private List<BrandVo> brands;
    private List<CatalogVo> catalogs;
    private List<AttrVo> attrs;

    public static class BrandVo {
        private Long id;
        private String name;
        private String img;
    }


    // 分类：
    public static class CatalogVo {
        private Long id;
        private String name;
    }

    public static class AttrVo {
        private Long id;
        private String name;
        private List<String> values;
    }
}
