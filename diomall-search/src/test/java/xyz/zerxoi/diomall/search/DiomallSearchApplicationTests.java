package xyz.zerxoi.diomall.search;

import static xyz.zerxoi.diomall.search.config.ElasticsearchConfig.COMMON_OPTIONS;

import java.io.IOException;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.alibaba.fastjson.JSON;

import lombok.Data;

@SpringBootTest
class DiomallSearchApplicationTests {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Test
    void indexTest() throws IOException {
        User user = new User("lucy", "female", 27);
        IndexRequest indexRequest = new IndexRequest("users")
                .id("ir9-QXwBw3XBTsgevjGK")
                .source(JSON.toJSONString(user), XContentType.JSON);
        restHighLevelClient.index(indexRequest, COMMON_OPTIONS);
    }

    @Test
    void searchTest() throws IOException {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.matchQuery("address", "mill"))
                .aggregation(AggregationBuilders.terms("ageAgg").field("age").size(100)
                        .subAggregation(AggregationBuilders.avg("balanceAvg").field("balance")));
        SearchRequest searchRequest = new SearchRequest().indices("accounts").source(sourceBuilder);
        SearchResponse response = restHighLevelClient.search(searchRequest, COMMON_OPTIONS);
        Terms ageAgg = response.getAggregations().get("ageAgg");
        for (Bucket bucket : ageAgg.getBuckets()) {
            System.out.println(bucket.getKeyAsString());
        }
    }

    @Data
    public static class User {
        private String name;
        private String gender;
        private Integer age;

        public User(String name, String gender, Integer age) {
            this.name = name;
            this.gender = gender;
            this.age = age;
        }
    }
}
