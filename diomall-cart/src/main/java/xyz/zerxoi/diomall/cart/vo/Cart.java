package xyz.zerxoi.diomall.cart.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class Cart {
    private List<CartItem> list;

    public Integer getCount() {
        return list.stream().mapToInt(CartItem::getCount).sum();
    }

    public Integer getCountType() {
        return list.size();
    }

    public BigDecimal getAmount() {
        return list.stream().map(CartItem::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add).subtract(getReduce());
    }

    public BigDecimal getReduce() {
        return BigDecimal.ZERO;
    }
}
