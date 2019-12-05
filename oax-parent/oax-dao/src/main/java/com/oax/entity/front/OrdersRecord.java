/**
 *
 */
package com.oax.entity.front;

import java.io.Serializable;
import java.math.BigDecimal;

import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelTarget;

/**
 * @author ：xiangwh
 * @ClassName:：OrdersRecord
 * @Description： 委托记录列表信息
 * @date ：2018年6月7日 下午3:58:37
 */
@ExcelTarget("ordersRecord")
public class OrdersRecord implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 8468400021859741509L;
    private Integer id;
    //userId
	/*private Integer userId;
	//marketId
	private Integer marketId;*/

    //交易对名称
    @Excel(name = "交易对", orderNum = "2", mergeVertical = true, isImportField = "marketName")
    private String marketName;

    //1买入 2卖出
    @Excel(name = "委托类型", orderNum = "3", replace = {"买入_1", "卖出_2"},
            mergeVertical = true, isImportField = "type")
    private Integer type;

    //交易价格
    @Excel(name = "价格", orderNum = "4", mergeVertical = true, isImportField = "price")
    private BigDecimal price;
    //交易量
    @Excel(name = "数量", orderNum = "5", mergeVertical = true, isImportField = "qty")
    private BigDecimal qty;
    // 状态-1已撤单 0待撮合 1撮合中 2已完成撮合 
    @Excel(name = "状态", orderNum = "8", replace = {"已撤销_-1", "待撮合_0", "部分成交_1", "已撮合_2"},
            mergeVertical = true, isImportField = "status")
    private String status;
    //成交比例
    @Excel(name = "成交比例", orderNum = "6", mergeVertical = true, isImportField = "tradeRate")
    private String tradeRate;
    //金额
    @Excel(name = "金额", orderNum = "7", mergeVertical = true, isImportField = "money")
    private BigDecimal money;
    //创建时间
    @Excel(name = "时间", orderNum = "1", mergeVertical = true, isImportField = "createTime")
    private String createTime;
    //价格精度
    private Integer priceDecimal;
    //数量精度
    private Integer qtyDecimal;
    
    

    public Integer getPriceDecimal() {
		return priceDecimal;
	}


	public void setPriceDecimal(Integer priceDecimal) {
		this.priceDecimal = priceDecimal;
	}


	public Integer getQtyDecimal() {
		return qtyDecimal;
	}


	public void setQtyDecimal(Integer qtyDecimal) {
		this.qtyDecimal = qtyDecimal;
	}


	public Integer getId() {
        return id;
    }


    public void setId(Integer id) {
        this.id = id;
    }


    public String getMarketName() {
        return marketName;
    }


    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }


    public Integer getType() {
        return type;
    }


    public void setType(Integer type) {
        this.type = type;
    }


    public BigDecimal getPrice() {
        return price;
    }


    public void setPrice(BigDecimal price) {
        this.price = price;
    }


    public BigDecimal getQty() {
        return qty;
    }


    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }


    public String getStatus() {
        return status;
    }


    public void setStatus(String status) {
        this.status = status;
    }


    public String getTradeRate() {
        return tradeRate;
    }


    public void setTradeRate(String tradeRate) {
        this.tradeRate = tradeRate;
    }


    public BigDecimal getMoney() {
        return money;
    }


    public void setMoney(BigDecimal money) {
        this.money = money;
    }


    public String getCreateTime() {
        return createTime;
    }


    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }


	@Override
	public String toString() {
		return "OrdersRecord [id=" + id + ", marketName=" + marketName + ", type=" + type + ", price=" + price
				+ ", qty=" + qty + ", status=" + status + ", tradeRate=" + tradeRate + ", money=" + money
				+ ", createTime=" + createTime + ", priceDecimal=" + priceDecimal + ", qtyDecimal=" + qtyDecimal + "]";
	}


    


}


