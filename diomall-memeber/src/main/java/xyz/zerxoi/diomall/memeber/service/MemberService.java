package xyz.zerxoi.diomall.memeber.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.zerxoi.common.utils.PageUtils;
import xyz.zerxoi.diomall.memeber.entity.MemberEntity;

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
}

