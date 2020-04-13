package com.hoangdieuctu.tools.klogs.service;

import com.hoangdieuctu.tools.klogs.constant.Constants;
import com.hoangdieuctu.tools.klogs.util.PodNameUtil;
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

    @Autowired
    private WSSenderService wsSenderService;

    private Map<String, WebSocket> sockets = new HashMap<>();
    private Map<WebSocket, String> sessions = new HashMap<>();

    public void connect(String pod) throws ApiException {
        if (sockets.containsKey(pod)) {
            logger.info("The connection to pod [{}] is already existed", pod);
            return;
        }

        logger.info("Open new connection to pod: {}", pod);
        String container = PodNameUtil.getContainerName(pod);
        String path = Constants.K8S_URL.replace("${pod}", pod).replace("${container}", container);

        Request request = buildRequest(path);
        WebSocket socket = apiClient.getHttpClient().newWebSocket(request, new WebSockets.Listener(new WebSockets.SocketListener() {

            private boolean failure = false;
            private WebSocket socket;

            @Override
            public void open(String protocol, WebSocket socket) {
                logger.info("Socket opened");
                this.socket = socket;
            }

            @Override
            public void close() {
                logger.info("Socket closed");
                LogService.this.close(socket, failure);
            }

            @Override
            public void bytesMessage(InputStream is) {
                try {
                    String message = IOUtils.toString(is);
                    if (StringUtils.isNotEmpty(message.trim())) {
                        wsSenderService.send(pod, message);
                    }
                } catch (IOException e) {
                    logger.error("Error while reading from stream. ", e.getMessage());
                }
            }

            @Override
            public void failure(Throwable t) {
                logger.info("Failure, something went wrong on the connection, closing the socket");
                this.failure = true;
            }

            @Override
            public void textMessage(Reader in) {
                logger.info("Receive text message but ignore");
            }
        }));

        sockets.put(pod, socket);
        sessions.put(socket, pod);
        logger.info("A new socket is opened, size: {}", sockets.size());
    }

    public void close(String pod) {
        WebSocket socket = sockets.remove(pod);
        if (socket != null) {
            sessions.remove(socket);
            socket.close(Constants.SOCKET_CLOSE_CODE, Constants.SOCKET_CLOSE_MESSAGE);
        }
    }

    public void close(WebSocket socket, boolean failure) {
        logger.info("Close socket, failure state: {}", failure);
        String session = sessions.remove(socket);
        if (session != null) {
            sockets.remove(session);
        }
    }


    public Request buildRequest(String path) throws ApiException {
        Map<String, String> headers = new HashMap<>();
        headers.put("Sec-WebSocket-Protocol", "v4.channel.k8s.io");
        headers.put("Connection", "Upgrade");
        headers.put("Upgrade", "SPDY/3.1");
        String[] auth = new String[]{"BearerToken"};
        return apiClient.buildRequest(path, "GET",
                Collections.emptyList(), Collections.emptyList(),
                null, headers, new HashMap<>(),
                new HashMap<>(), auth, null);
    }
}
