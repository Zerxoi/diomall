package xyz.zerxoi.diomall.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.zerxoi.common.utils.PageUtils;
import xyz.zerxoi.common.utils.Query;

import xyz.zerxoi.diomall.member.dao.MemberDao;
import xyz.zerxoi.diomall.member.entity.MemberEntity;
import xyz.zerxoi.diomall.member.service.MemberService;
import xyz.zerxoi.diomall.member.vo.GithubUserVo;
import xyz.zerxoi.diomall.member.vo.MemberLoginVo;
import xyz.zerxoi.diomall.member.vo.MemberRegisterVo;


@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberEntity> page = this.page(
                new Query<MemberEntity>().getPage(params),
                new QueryWrapper<MemberEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void register(MemberRegisterVo registerVo) {
        checkPhoneUnique(registerVo.getPhone());
        checkUserNameUnique(registerVo.getUserName());

        MemberEntity entity = new MemberEntity();
        entity.setUsername(registerVo.getUserName());
        entity.setMobile(registerVo.getPhone());
        entity.setCreateTime(new Date());
        // 密码加密
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodePassword = passwordEncoder.encode(registerVo.getPassword());
        entity.setPassword(encodePassword);

        save(entity);
    }

    @Override
    public MemberEntity login(MemberLoginVo loginVo) {
        String loginAccount = loginVo.getLoginAccount();
        //以用户名或电话号登录的进行查询
        MemberEntity entity = getOne(new LambdaQueryWrapper<MemberEntity>().eq(MemberEntity::getUsername, loginAccount)
                .or().eq(MemberEntity::getMobile, loginAccount));
        if (entity != null) {
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            boolean matches = bCryptPasswordEncoder.matches(loginVo.getPassword(), entity.getPassword());
            if (matches) {
                entity.setPassword("");
            } else {
                entity = null;
            }
        }
        return entity;
    }

    @Override
    public MemberEntity login(GithubUserVo githubUserVo) {
        MemberEntity member = getOne(new LambdaQueryWrapper<MemberEntity>().eq(MemberEntity::getUid, githubUserVo.getId()));
        if (member == null) {
            member = new MemberEntity();
            member.setUid(Long.toString(githubUserVo.getId()));
            member.setNickname(githubUserVo.getLogin());
            member.setHeader(githubUserVo.getAvatarUrl());
            save(member);
        }
        return member;
    }

    private void checkUserNameUnique(String userName) {
        if (count(new LambdaQueryWrapper<MemberEntity>().eq(MemberEntity::getUsername, userName)) > 0) {
            // TODO 随便抛出的异常
            throw new RuntimeException("用户名已存在");
        }
    }

    private void checkPhoneUnique(String phone) {
        if (count(new LambdaQueryWrapper<MemberEntity>().eq(MemberEntity::getMobile, phone)) > 0) {
            // TODO 随便抛出的异常
            throw new RuntimeException("手机号已存在");
        }
    }


}