package com.hoangdieuctu.tools.dlogs.util;

import com.hoangdieuctu.tools.dlogs.constant.Constants;
import io.kubernetes.client.openapi.models.V1Pod;

public class PodNameUtil {

    public static String getPodName(V1Pod pod) {
        return pod.getMetadata().getName();
    }

    public static String getServiceName(String pod) {
        return pod.substring(0, pod.indexOf(Constants.DEFAULT_POD_NAME_SEPARATE));
    }

    public static String getContainerName(String pod) {
        return getServiceName(pod) + Constants.DEFAULT_POD_NAME_SEPARATE + Constants.DEFAULT_POD_CONTAINER_SUFFIX;
    }
}
