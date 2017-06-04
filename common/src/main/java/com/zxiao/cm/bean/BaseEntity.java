package com.zxiao.cm.bean;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * 所有实体的公共父类
 *
 * @author xiong.yu
 */
public abstract class BaseEntity implements Serializable {

    /**
     * JSR-303原生支持如下限制:
     *
     * @Null 限制只能为null
     * @NotNull 限制必须不为null
     * @AssertFalse 限制必须为false
     * @AssertTrue 限制必须为true
     * @DecimalMax(value) 限制必须为一个不大于指定值的数字
     * @DecimalMin(value) 限制必须为一个不小于指定值的数字
     * @Digits(integer,fraction) 限制必须为一个小数，且整数部分的位数不能超过integer，小数部分的位数不能超过fraction
     * @Future 限制必须是一个将来的日期
     * @Max(value) 限制必须为一个不大于指定值的数字
     * @Min(value) 限制必须为一个不小于指定值的数字
     * @Past 限制必须是一个过去的日期
     * @Pattern(value) 限制必须符合指定的正则表达式
     * @Size(max,min) 限制字符长度必须在min到max之间
     */

    public abstract <T extends Serializable> T getPK();

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    /**
     * 把空字符串字段置为null
     */
    public void setEmptyNull() {
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field temp : fields) {
            temp.setAccessible(true);
            try {
                Object value = temp.get(this);
                if (temp.getType().isAssignableFrom(String.class) && value != null && StringUtils.isEmpty(value.toString())) {

                    temp.set(this, null);
                }
            } catch (Exception e) {
            }
        }
    }


}
