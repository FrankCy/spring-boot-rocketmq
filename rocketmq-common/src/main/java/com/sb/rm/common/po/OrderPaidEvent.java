package com.sb.rm.common.po;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @version 1.0
 * @description：
 * @author: Yang.Chang
 * @project: spring-boot-rocketmq
 * @package: com.sb.rm.po、
 * @email: cy880708@163.com
 * @date: 2019/3/25 下午6:06
 * @mofified By:
 */
public class OrderPaidEvent implements Serializable {

    private static final long serialVersionUID = 7268611260352768861L;

    private String orderId;

    private BigDecimal paidMoney;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getPaidMoney() {
        return paidMoney;
    }

    public void setPaidMoney(BigDecimal paidMoney) {
        this.paidMoney = paidMoney;
    }

    public OrderPaidEvent() {
    }

    public OrderPaidEvent(String orderId, BigDecimal paidMoney) {
        this.orderId = orderId;
        this.paidMoney = paidMoney;
    }

    @Override
    public String toString() {
        return "OrderPaidEvent{" +
                "orderId='" + orderId + '\'' +
                ", paidMoney=" + paidMoney +
                '}';
    }
}
