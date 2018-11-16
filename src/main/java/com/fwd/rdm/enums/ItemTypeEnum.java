package com.fwd.rdm.enums;

/**
 * @Author: fanwd
 * @Description:
 * @Date: Create in 10:48 2018/11/10
 */
public enum ItemTypeEnum {
    // 根节点
    ROOT,
    // 服务器节点
    SERVER,
    // 数据库节点
    DATABASE,
    // 数据KEY节点
    KEY;

    public boolean belongs(ItemTypeEnum... itemTypeEnums) {
        for (ItemTypeEnum itemTypeEnum : itemTypeEnums) {
            if (itemTypeEnum.equals(this)) {
                return true;
            }
        }
        return false;
    }
}
