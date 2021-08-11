package xyz.zerxoi.diomall.memeber.dao;

import xyz.zerxoi.diomall.memeber.entity.GrowthChangeHistoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 成长值变化历史记录
 * 
 * @author zerxoi
 * @email zerxoi1997@gmail.com
 * @date 2021-08-11 23:35:29
 */
@Mapper
public interface GrowthChangeHistoryDao extends BaseMapper<GrowthChangeHistoryEntity> {
	
}
