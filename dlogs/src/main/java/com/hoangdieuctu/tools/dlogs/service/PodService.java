package com.hoangdieuctu.tools.dlogs.service;

import com.hoangdieuctu.tools.dlogs.repository.PodRepository;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.models.V1Pod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PodService {

    @Autowired
    private PodRepository podRepository;

    public List<V1Pod> getDefaultPods() throws ApiException {
        return podRepository.getDefaultPods();
    }

}
