package xyz.zerxoi.diomall.auth.vo;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class GithubAccessTokenVo {
    private String accessToken;
    private String scope;
    private String tokenType;
}
