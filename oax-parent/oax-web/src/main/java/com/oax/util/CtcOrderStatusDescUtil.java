package com.oax.util;

import com.oax.entity.enums.CtcOrderStatus;
import com.oax.entity.enums.CtcOrderType;
import org.springframework.stereotype.Component;

/**
 * c2c订单状态描述  根据买入和卖出，用户和商家来获取不同的描述文案
 * 状态文案有 6 * 4 = 24种
 */
@Component
public class CtcOrderStatusDescUtil {

    public static String getStatusDesc(CtcOrderStatus status, CtcOrderType type, boolean isUser) {
        if (type == CtcOrderType.USER_BUY) {
            switch (status) {
                case NO_PAY:
                    if (isUser) {
                        return "未付款";
                    } else {
                        return "未付款";
                    }
                case PAYED:
                    if (isUser) {
                        return "处理中";
                    } else {
                        return "处理中";
                    }
                case APPEAL:
                    if (isUser) {
                        return "申诉中";
                    } else {
                        return "申诉中";
                    }
                case PROCESSING:
                    if (isUser) {
                        return "申诉处理中";
                    } else {
                        return "申诉处理中";
                    }
                case FINISH:
                    if (isUser) {
                        return "交易完成";
                    } else {
                        return "交易完成";
                    }
                case CANCEL:
                    if (isUser) {
                        return "已取消";
                    } else {
                        return "已取消";
                    }
            }
        } else if (type == CtcOrderType.USER_SALE) {
            switch (status) {
                case NO_PAY:
                    if (isUser) {
                        return "处理中";
                    } else {
                        return "处理中";
                    }
                case PAYED:
                    if (isUser) {
                        return "商家已付款";
                    } else {
                        return "用户确认中";
                    }
                case APPEAL:
                    if (isUser) {
                        return "申诉中";
                    } else {
                        return "申诉中";
                    }
                case PROCESSING:
                    if (isUser) {
                        return "申诉处理中";
                    } else {
                        return "申诉中";
                    }
                case FINISH:
                    if (isUser) {
                        return "交易完成";
                    } else {
                        return "交易完成";
                    }
                case CANCEL:
                    if (isUser) {
                        return "已取消";
                    } else {
                        return "交易完成";
                    }
            }
        }
        return "";
    }

    public static void main(String[] args) {
        System.out.println(getStatusDesc(CtcOrderStatus.NO_PAY, CtcOrderType.USER_SALE, true));
    }

}
