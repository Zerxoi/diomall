package xyz.zerxoi.diomall.thirdparty;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.aliyun.oss.OSS;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.aliyun.oss.OSSClient;

@SpringBootTest
class DiomallThirdPartyApplicationTests {

    @Autowired
    private OSS ossClient;

    @Test
    void contextLoads() {
    }

    @Test
    public void ossTest() throws FileNotFoundException {
        // 填写本地文件的完整路径。如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件流。
        InputStream inputStream = new FileInputStream("C:/Users/Zerxoi/Downloads/example.txt");
        // 依次填写Bucket名称（例如examplebucket）和Object完整路径（例如exampledir/exampleobject.txt）。Object完整路径中不能包含Bucket名称。
        ossClient.putObject("diomall", "exampledir/example.txt", inputStream);
    }

}
