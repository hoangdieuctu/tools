## KLogs ##

### Overview ###
We all use **kubectl logs** to help us debug and monitor our applications. 
When a containerized application writes to stdout and stderr, the container engine handles and redirects these streams to a logging driver, which by default in Kubernetes is written to a file in json format.

![picture](k8s-log.png)

KLogs is using the websocket technology, it attach a stream to stdout of a kubernetes pod and forward the output to the UI.

![picture](klogs.png)

### How to run ###

1. Run on local: It require the kubernetes config file on ~/.kube/config
2. Run on docker: Should mount the kubernetes to docker machine
```bash
docker run -p 10100:8080 -d --name klogs -v /Users/hoangdieuctu/.kube/config:/root/.kube/config klogs
```