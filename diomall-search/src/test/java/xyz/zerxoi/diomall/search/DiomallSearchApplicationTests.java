package xyz.zerxoi.diomall.search;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.math.BigDecimal;

@SpringBootTest
class DiomallSearchApplicationTests {

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    @Test
    void indexTest() throws IOException {
        User user = new User("lucy", "female", 27);
        elasticsearchClient.create((builder -> builder.index("users").id("ir9-QXwBw3XBTsgevjGK").document(user)));
    }

    @Test
    void searchTest() throws IOException {
        SearchResponse<Account> search = elasticsearchClient.search(builder -> builder.index("accounts")
                .query(query -> query.match(match -> match.field("address").query(matchQuery -> matchQuery.stringValue("mill"))))
                .aggregations("ageAgg", ageAgg -> ageAgg.terms(terms -> terms.field("age").size(100))
                        .aggregations("balanceAvg", balanceAvg -> balanceAvg.avg(avg -> avg.field("balance")))),
                Account.class);
        search.aggregations().forEach((s, aggregate) -> {
            System.out.println(s);
            System.out.println(aggregate);
        });
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

    @Data
    public static class Account {
        private Long accountNumber;
        private String address;
        private Integer age;
        private BigDecimal balance;
        private String city;
        private String email;
        private String employer;
        private String firstname;
        private String lastname;
        private String gender;
        private String state;
    }
}
