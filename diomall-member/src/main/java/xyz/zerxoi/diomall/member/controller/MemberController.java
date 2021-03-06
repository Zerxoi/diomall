package xyz.zerxoi.diomall.member.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import xyz.zerxoi.common.utils.PageUtils;
import xyz.zerxoi.common.utils.R;
import xyz.zerxoi.diomall.member.entity.MemberEntity;
import xyz.zerxoi.diomall.member.feign.CouponFeignService;
import xyz.zerxoi.diomall.member.service.MemberService;
import xyz.zerxoi.diomall.member.vo.GithubUserVo;
import xyz.zerxoi.diomall.member.vo.MemberLoginVo;
import xyz.zerxoi.diomall.member.vo.MemberRegisterVo;


/**
 * 会员
 *
 * @author zerxoi
 * @email zerxoi1997@gmail.com
 * @date 2021-08-11 23:35:29
 */
@RefreshScope
@RestController
@RequestMapping("member/member")
public class MemberController {
    @Autowired
    private MemberService memberService;
    @Autowired
    private CouponFeignService couponFeignService;

    @Value("${diomall.user.name}")
    private String name;
    @Value("${diomall.user.age}")
    private Integer age;

    @RequestMapping("/config")
    public R config() {
        return R.ok().put("name", name).put("age", age);
    }

    @RequestMapping("/test")
    public R test() {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setNickname("pony");
        return couponFeignService.feign().put("member", memberEntity);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = memberService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        MemberEntity member = memberService.getById(id);

        return R.ok().put("member", member);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody MemberEntity member) {
        memberService.save(member);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody MemberEntity member) {
        memberService.updateById(member);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        memberService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    @RequestMapping("/register")
    public R register(@RequestBody MemberRegisterVo registerVo) {
        try {
            memberService.register(registerVo);
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }
        return R.ok();
    }

    @RequestMapping("/login")
    public R login(@RequestBody MemberLoginVo loginVo) {
        MemberEntity memberEntity = memberService.login(loginVo);
        if (memberEntity == null) {
            return R.error("登陆失败");
        }
        return R.ok().put("data", memberEntity);
    }

    @RequestMapping("/github/login")
    public R login(@RequestBody GithubUserVo githubUserVo) {
        MemberEntity memberEntity = memberService.login(githubUserVo);
        return R.ok().put("data", memberEntity);
    }

}
