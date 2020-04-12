package com.hoangdieuctu.tools.dlogs.repository;

import com.hoangdieuctu.tools.dlogs.constant.Constants;
import com.hoangdieuctu.tools.dlogs.util.PodNameUtil;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class PodRepository {

    @Autowired
    private CoreV1Api coreApi;

    public List<V1Pod> getAllPods() throws ApiException {
        V1PodList podList = coreApi.listPodForAllNamespaces(null, null, null, null, null, null, null, null, null);
        return podList.getItems();
    }

    public List<V1Pod> getDefaultPods() throws ApiException {
        List<V1Pod> pods = getAllPods();
        return pods.stream()
                .filter(pod -> Constants.DEFAULT_NAMESPACE.equals(PodNameUtil.getPodName(pod)))
                .collect(Collectors.toList());
    }
}
