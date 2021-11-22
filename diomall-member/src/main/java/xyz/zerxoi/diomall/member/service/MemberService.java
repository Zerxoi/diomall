package xyz.zerxoi.diomall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.zerxoi.common.utils.PageUtils;
import xyz.zerxoi.diomall.member.entity.MemberEntity;
import xyz.zerxoi.diomall.member.vo.GithubUserVo;
import xyz.zerxoi.diomall.member.vo.MemberLoginVo;
import xyz.zerxoi.diomall.member.vo.MemberRegisterVo;

import java.util.Map;

/**
 * 会员
 *
 * @author zerxoi
 * @email zerxoi1997@gmail.com
 * @date 2021-08-11 23:35:29
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void register(MemberRegisterVo registerVo) throws RuntimeException;

    MemberEntity login(MemberLoginVo loginVo);

    MemberEntity login(GithubUserVo githubUserVo);
}

