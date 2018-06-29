package com.qiniu.pandora.pipeline.repo;

/**
 * Created by jemy on 2018/6/25.
 * workflow 状态
 */
public interface WorkflowStatus {
    String WorkflowReady = "Ready";    // 新建状态
    String WorkflowStarting = "Starting"; // 存在任一资源为 Starting
    String WorkflowStarted = "Started"; // 所有资源为 Started
    String WorkflowStopping = "Stopping";// 存在任一资源为 Stopping
    String WorkflowStopped = "Stopped"; // 所有资源为 Stopped
    String WorkflowUnknown = "Unknown"; // 获取状态失败时的异常状态，
}