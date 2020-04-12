package com.hoangdieuctu.tools.dlogs.service;

import com.hoangdieuctu.tools.dlogs.constant.Constants;
import com.hoangdieuctu.tools.dlogs.util.PodNameUtil;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.util.WebSockets;
import okhttp3.Request;
import okhttp3.WebSocket;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class LogService {

    private static Logger logger = LoggerFactory.getLogger(LogService.class);

    @Autowired
    private ApiClient apiClient;

    private Map<String, WebSocket> sockets = new HashMap<>();

    public void connect(String session, String pod) throws ApiException {
        String container = PodNameUtil.getContainerName(pod);
        String path = Constants.K8S_URL.replace("${pod}", pod).replace("${container}", container);

        Request request = buildRequest(path);
        WebSocket socket = apiClient.getHttpClient().newWebSocket(request, new WebSockets.Listener(new WebSockets.SocketListener() {
            @Override
            public void open(String protocol, WebSocket socket) {
                logger.info("Socket opened");
            }

            @Override
            public void close() {
                logger.info("Socket closed");
            }

            @Override
            public void bytesMessage(InputStream is) {
                try {
                    String message = IOUtils.toString(is);
                    if (StringUtils.isNotEmpty(message.trim())) {
                        logger.info("Received: {}", message);
                    }
                } catch (IOException e) {
                    logger.error("Error while reading from stream. ", e);
                }
            }

            @Override
            public void failure(Throwable t) {
                logger.info("Connect failed. ", t);
            }

            @Override
            public void textMessage(Reader in) {
                logger.info("Receive text message");
            }
        }));

        this.sockets.put(session, socket);
        logger.info("A new socket is opened, size: {}", this.sockets.size());
    }

    public void close(String session) {
        WebSocket socket = sockets.remove(session);
        if (socket != null) {
            logger.info("A socket was closed: {}", session);
            socket.close(Constants.SOCKET_CLOSE_CODE, Constants.SOCKET_CLOSE_MESSAGE);
        }
    }


    public Request buildRequest(String path) throws ApiException {
        Map<String, String> headers = new HashMap<>();
        headers.put("Sec-WebSocket-Protocol", "v4.channel.k8s.io");
        headers.put("Connection", "Upgrade");
        headers.put("Upgrade", "SPDY/3.1");
        String[] localVarAuthNames = new String[]{"BearerToken"};
        return apiClient.buildRequest(path, "GET",
                Collections.emptyList(), Collections.emptyList(), null,
                headers, new HashMap<>(), new HashMap<>(),
                localVarAuthNames, null);
    }
}
