# 本地单机启动

这里使用的是`docker-compose`启动

启动流程 nameserver -> broker -> console

执行 `chmod u+x start.sh && ./start.sh` 启动

控制台[地址](http://localhost:8180/#/)

注意： `broker.conf`中的`brokerIP1`每次都要改为本机的IP才行

### ref

* https://github.com/foxiswho/docker-rocketmq
* https://www.runoob.com/docker/docker-compose.html
* https://github.com/apache/rocketmq-dashboard
* [docker-compose命令](https://pythondjango.cn/python/tools/3-docker-compose/)
