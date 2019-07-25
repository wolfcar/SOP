package com.gitee.sop.servercommon.mapping;

import org.springframework.util.StringUtils;

/**
 * @author tanghc
 */
public class MappingUtil {

    public static final char SEPARATOR_CHAR = '/';

    /**
     * 将springmvc接口路径转换成SOP方法名
     * @param path springmvc路径，如/a/b,/goods/listGoods
     * @return 返回接口方法名，/goods/listGoods ->  goods.listGoods
     */
    public static String buildApiName(String path) {
        path = StringUtils.trimLeadingCharacter(path, SEPARATOR_CHAR);
        path = StringUtils.trimTrailingCharacter(path, SEPARATOR_CHAR);
        path = path.replace(SEPARATOR_CHAR, '.');
        return path;
    }

}
