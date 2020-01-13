package com.gitee.sop.gatewaycommon.route;

import lombok.Data;

/**
 * @author tanghc
 */
@Data
public class ForwardInfo {
    private String path;
    private String version;
    private String domain;
}
