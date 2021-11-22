package xyz.zerxoi.diomall.auth.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import xyz.zerxoi.common.utils.R;
import xyz.zerxoi.diomall.auth.vo.GithubUserVo;
import xyz.zerxoi.diomall.auth.vo.UserLoginVo;
import xyz.zerxoi.diomall.auth.vo.UserRegisterVo;

@FeignClient("diomall-member")
public interface MemberFeignService {
    @RequestMapping("member/member/github/login")
    R login(@RequestBody GithubUserVo githubUserVo);

    @RequestMapping("member/member/login")
    R login(@RequestBody UserLoginVo userLoginVo);

    @RequestMapping("member/member/register")
    R register(@RequestBody UserRegisterVo registerVo) throws RuntimeException;
}
