package com.hoangdieuctu.tools.dlogs.constant;

public class Constants {
    public static final String CONFIG_FILE = "/Users/hoangdieuctu/.kube/config";

    public static final String K8S_URL = "/api/v1/namespaces/default/pods/${pod}/attach?stdout=true&container=${container}";

    public static final String DEFAULT_NAMESPACE = "default";
    public static final String DEFAULT_POD_NAME_SEPARATE = "-";
    public static final String DEFAULT_POD_CONTAINER_SUFFIX = "container";

    public static final Integer SOCKET_CLOSE_CODE = 3000;
    public static final String SOCKET_CLOSE_MESSAGE = "Normal closed.";

    public static final String SPLASH = "/";
    public static final String TOPIC_PREFIX = "/topic/";

    public static final String POD_RUNNING_STATE = "Running";
}
