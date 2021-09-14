package xyz.zerxoi.diomall.product;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.aliyun.oss.OSSClient;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import xyz.zerxoi.diomall.product.entity.BrandEntity;
import xyz.zerxoi.diomall.product.service.BrandService;

@SpringBootTest
class DiomallProductApplicationTests {
    @Autowired
    BrandService brandService;
    @Autowired
    private OSSClient ossClient;

    @Test
    void contextLoads() {
        BrandEntity entity = new BrandEntity();
        entity.setName("HUAWEI");
        System.out.println(brandService.list(new QueryWrapper<BrandEntity>().eq("brand_id", 1)));
    }

    @Test
    public void ossTest() throws FileNotFoundException {
        // 填写本地文件的完整路径。如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件流。
        InputStream inputStream = new FileInputStream("/Users/zouxin/Downloads/Java开发手册（嵩山版）.pdf");
        // 依次填写Bucket名称（例如examplebucket）和Object完整路径（例如exampledir/exampleobject.txt）。Object完整路径中不能包含Bucket名称。
        ossClient.putObject("diomall", "exampledir/exampleobject.pdf", inputStream);
    }

}
