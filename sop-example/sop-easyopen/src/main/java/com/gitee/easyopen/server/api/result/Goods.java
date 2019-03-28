package com.gitee.easyopen.server.api.result;

import com.gitee.easyopen.doc.DataType;
import com.gitee.easyopen.doc.annotation.ApiDocField;

import java.math.BigDecimal;

public class Goods {

    @ApiDocField(description = "id")
    private Long id;
    @ApiDocField(description = "商品名称")
    private String goods_name;
    @ApiDocField(description = "价格", dataType = DataType.FLOAT)
    private BigDecimal price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Goods [id=" + id + ", goods_name=" + goods_name + ", price=" + price + "]";
    }

}
