package xyz.zerxoi.diomall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.zerxoi.common.utils.PageUtils;
import xyz.zerxoi.diomall.member.entity.MemberStatisticsInfoEntity;

import java.util.Map;

/**
 * 会员统计信息
 *
 * @author zerxoi
 * @email zerxoi1997@gmail.com
 * @date 2021-08-11 23:35:29
 */
public interface MemberStatisticsInfoService extends IService<MemberStatisticsInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

