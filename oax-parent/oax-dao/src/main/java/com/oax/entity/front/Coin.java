package com.oax.entity.front;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * coin
 *
 * @author
 */
public class Coin implements Serializable {
    private Integer id;

    /**
     * 币种类型：1eth代币 2 BTC 3 EHT_TOKEN
     */
    private Integer type;

    /**
     * 图标
     */
    private String image;

    /**
     * 英文简称
     */
    private String shortName;

    /**
     * 英文全称
     */
    private String fullName;

    /**
     * 小数位
     */
    private Integer decimals;

    /**
     * 合约地址
     */
    private String contractAddress;

    /**
     * 合约转账方法id
     */
    private String methodId;

    /**
     * 服务器ip
     */
    private String serverIp;

    /**
     * 服务器rpc端口
     */
    private Integer serverPort;

    /**
     * 平台主地址
     */
    private String mainAddress;

    /**
     * 平台主地址密码
     */
    private String mainAddressPassword;

    /**
     * Gwei
     */
    private Integer gasPrice;

    /**
     * 最大确认数
     */
    private Integer gasLimit;

    /**
     * 冷钱包地址
     */
    private String coldAddress;

    /**
     * 超过多少数量转入冷钱包
     */
    private BigDecimal outQtyToColdAddress;

    /**
     * 允许充值 0：否 1：是
     */
    private Integer allowRecharge;

    /**
     * 允许提币 0：否 1：是
     */
    private Integer allowWithdraw;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 转出手续费
     */
    private BigDecimal withdrawFee;

    /**
     * 提币最大限制
     */
    private BigDecimal maxOutQty;

    /**
     * 提币最小限制
     */
    private BigDecimal minOutQty;

    /**
     * 预警值
     */
    private BigDecimal warningQty;
    /**
     * 中文名
     */
    private String cnName;

    /**
     * 超过多少传入平台主地址
     */
    private BigDecimal outQtyToMainAddress;

    /**
     * btc-帐户名
     */
    private String serverUsername;

    /**
     * btc-帐户密码
     */
    private String serverPassword;
    /**
     * 父币id
     */
    private Integer parentId;

    /**
     * omni-token标识
     */
    private Integer propertyid;
    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }


    public Integer getPropertyid() {
        return propertyid;
    }

    public void setPropertyid(Integer propertyid) {
        this.propertyid = propertyid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getDecimals() {
        return decimals;
    }

    public void setDecimals(Integer decimals) {
        this.decimals = decimals;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public String getMethodId() {
        return methodId;
    }

    public void setMethodId(String methodId) {
        this.methodId = methodId;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public Integer getServerPort() {
        return serverPort;
    }

    public void setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
    }

    public String getMainAddress() {
        return mainAddress;
    }

    public void setMainAddress(String mainAddress) {
        this.mainAddress = mainAddress;
    }

    public String getMainAddressPassword() {
        return mainAddressPassword;
    }

    public void setMainAddressPassword(String mainAddressPassword) {
        this.mainAddressPassword = mainAddressPassword;
    }

    public Integer getGasPrice() {
        return gasPrice;
    }

    public void setGasPrice(Integer gasPrice) {
        this.gasPrice = gasPrice;
    }

    public Integer getGasLimit() {
        return gasLimit;
    }

    public void setGasLimit(Integer gasLimit) {
        this.gasLimit = gasLimit;
    }

    public String getColdAddress() {
        return coldAddress;
    }

    public void setColdAddress(String coldAddress) {
        this.coldAddress = coldAddress;
    }

    public BigDecimal getOutQtyToColdAddress() {
        return outQtyToColdAddress;
    }

    public void setOutQtyToColdAddress(BigDecimal outQtyToColdAddress) {
        this.outQtyToColdAddress = outQtyToColdAddress;
    }

    public Integer getAllowRecharge() {
        return allowRecharge;
    }

    public void setAllowRecharge(Integer allowRecharge) {
        this.allowRecharge = allowRecharge;
    }

    public Integer getAllowWithdraw() {
        return allowWithdraw;
    }

    public void setAllowWithdraw(Integer allowWithdraw) {
        this.allowWithdraw = allowWithdraw;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public BigDecimal getWithdrawFee() {
        return withdrawFee;
    }

    public void setWithdrawFee(BigDecimal withdrawFee) {
        this.withdrawFee = withdrawFee;
    }

    public BigDecimal getMaxOutQty() {
        return maxOutQty;
    }

    public void setMaxOutQty(BigDecimal maxOutQty) {
        this.maxOutQty = maxOutQty;
    }

    public BigDecimal getMinOutQty() {
        return minOutQty;
    }

    public void setMinOutQty(BigDecimal minOutQty) {
        this.minOutQty = minOutQty;
    }

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

    public BigDecimal getOutQtyToMainAddress() {
        return outQtyToMainAddress;
    }

    public void setOutQtyToMainAddress(BigDecimal outQtyToMainAddress) {
        this.outQtyToMainAddress = outQtyToMainAddress;
    }

    public String getServerUsername() {
        return serverUsername;
    }

    public void setServerUsername(String serverUsername) {
        this.serverUsername = serverUsername;
    }

    public String getServerPassword() {
        return serverPassword;
    }

    public void setServerPassword(String serverPassword) {
        this.serverPassword = serverPassword;
    }


    public BigDecimal getWarningQty() {
        return warningQty;
    }

    public void setWarningQty(BigDecimal warningQty) {
        this.warningQty = warningQty;
    }

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
    
}