package com.gitee.sop.adminserver.common;

/**
 * @author tanghc
 */
public class ZookeeperPathNotExistException extends Exception {
    public ZookeeperPathNotExistException(String message) {
        super(message);
    }
}
