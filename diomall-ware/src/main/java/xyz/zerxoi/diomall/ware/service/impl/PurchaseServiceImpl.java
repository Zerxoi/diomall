package xyz.zerxoi.diomall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.zerxoi.common.constant.WareConstant;
import xyz.zerxoi.common.utils.PageUtils;
import xyz.zerxoi.common.utils.Query;
import xyz.zerxoi.diomall.ware.dao.PurchaseDao;
import xyz.zerxoi.diomall.ware.entity.PurchaseDetailEntity;
import xyz.zerxoi.diomall.ware.entity.PurchaseEntity;
import xyz.zerxoi.diomall.ware.service.PurchaseDetailService;
import xyz.zerxoi.diomall.ware.service.PurchaseService;
import xyz.zerxoi.diomall.ware.service.WareSkuService;
import xyz.zerxoi.diomall.ware.vo.MergeVo;
import xyz.zerxoi.diomall.ware.vo.PurchaseDoneVo;
import xyz.zerxoi.diomall.ware.vo.PurchaseItemDoneVo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {
    @Autowired
    private PurchaseDetailService purchaseDetailService;
    @Autowired
    private WareSkuService wareSkuService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageUnreceivePurchase(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new LambdaQueryWrapper<PurchaseEntity>().eq(PurchaseEntity::getStatus, 0).or()
                        .eq(PurchaseEntity::getStatus, 1)
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional
    public boolean mergePurchase(MergeVo mergeVo) {
        List<PurchaseDetailEntity> purchaseDetailEntities = purchaseDetailService
                .list(new LambdaQueryWrapper<PurchaseDetailEntity>()
                        .in(PurchaseDetailEntity::getId, mergeVo.getItems()));
        // 判断是否所有采购需求都是创建
        if (purchaseDetailEntities.stream().allMatch(detailEntity ->
                detailEntity.getStatus().equals(WareConstant.PurchaseDetailStatusEnum.CREATED.getCode()))) {
            // 创建或者更新采购单
            Long purchaseId = mergeVo.getPurchaseId();
            if (purchaseId == null) {
                PurchaseEntity purchaseEntity = new PurchaseEntity();
                purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.CREATED.getCode());
                purchaseEntity.setCreateTime(new Date());
                purchaseEntity.setUpdateTime(new Date());
                save(purchaseEntity);
                purchaseId = purchaseEntity.getId();
            } else {
                update(new LambdaUpdateWrapper<PurchaseEntity>().set(PurchaseEntity::getUpdateTime, new Date())
                        .eq(PurchaseEntity::getId, purchaseId));
            }

            // 更新采购需求
            for (PurchaseDetailEntity detailEntity : purchaseDetailEntities) {
                detailEntity.setPurchaseId(purchaseId);
                detailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.ASSIGNED.getCode());
            }
            purchaseDetailService.updateBatchById(purchaseDetailEntities);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void received(List<Long> ids) {
        //1、确认当前采购单是新建或者已分配状态
        List<PurchaseEntity> collect = ids.stream().map(this::getById)
                .filter(item -> item.getStatus() == WareConstant.PurchaseStatusEnum.CREATED.getCode() ||
                        item.getStatus() == WareConstant.PurchaseStatusEnum.ASSIGNED.getCode())
                .peek(item -> {
                    item.setStatus(WareConstant.PurchaseStatusEnum.RECEIVE.getCode());
                    item.setUpdateTime(new Date());
                }).collect(Collectors.toList());

        //2、改变采购单的状态
        this.updateBatchById(collect);


        //3、改变采购项的状态
        collect.forEach((item) -> {
            List<PurchaseDetailEntity> entities = purchaseDetailService.listDetailByPurchaseId(item.getId());
            List<PurchaseDetailEntity> detailEntities = entities.stream()
                    .peek(entity -> entity.setStatus(WareConstant.PurchaseDetailStatusEnum.BUYING.getCode()))
                    .collect(Collectors.toList());
            purchaseDetailService.updateBatchById(detailEntities);
        });
    }

    @Override
    @Transactional
    public void done(PurchaseDoneVo doneVo) {
        Long id = doneVo.getId();

        // 改变采购项的状态
        boolean flag = true;
        List<PurchaseItemDoneVo> items = doneVo.getItems();

        List<PurchaseDetailEntity> updates = new ArrayList<>();
        for (PurchaseItemDoneVo item : items) {
            PurchaseDetailEntity detailEntity = new PurchaseDetailEntity();
            if (item.getStatus() == WareConstant.PurchaseDetailStatusEnum.HASERROR.getCode()) {
                flag = false;
                detailEntity.setStatus(item.getStatus());
            } else {
                detailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.FINISH.getCode());
                // 将成功采购的进行入库
                PurchaseDetailEntity entity = purchaseDetailService.getById(item.getItemId());
                wareSkuService.addStock(entity.getSkuId(), entity.getWareId(), entity.getSkuNum());
            }
            detailEntity.setId(item.getItemId());
            updates.add(detailEntity);
        }

        purchaseDetailService.updateBatchById(updates);

        // 改变采购单状态
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(id);
        purchaseEntity.setStatus(flag ? WareConstant.PurchaseStatusEnum.FINISH.getCode() :
                WareConstant.PurchaseStatusEnum.HASERROR.getCode());
        purchaseEntity.setUpdateTime(new Date());
        this.updateById(purchaseEntity);
    }

}