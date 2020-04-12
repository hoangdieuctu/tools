package com.hoangdieuctu.tools.klogs.service;

import com.hoangdieuctu.tools.klogs.constant.Constants;
import com.hoangdieuctu.tools.klogs.repository.PodRepository;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.models.V1Pod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PodService {

    @Autowired
    private PodRepository podRepository;


    public List<V1Pod> getDefaultPods() throws ApiException {
        return podRepository.getDefaultPods();
    }

    @Cacheable(value = "pods")
    public List<V1Pod> getRunningDefaultPods() throws ApiException {
        List<V1Pod> pods = getDefaultPods();
        return pods.stream().filter(pod -> Constants.POD_RUNNING_STATE.equals(pod.getStatus().getPhase()))
                .collect(Collectors.toList());
    }
}
