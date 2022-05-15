#!/usr/bin/env bash


# #############################################################################
# File Name   : start.sh
# Author      : DaiDai
# Mail        : daidai4269@aliyun.com
# Created Time: 日  5/15 14:01:17 2022
# #############################################################################

set -xe

# 创建目录
mkdir -p ./rmqs/logs
mkdir -p ./rmqs/store
mkdir -p ./rmq/logs
mkdir -p ./rmq/store

# 设置目录权限
chmod -R 777 ./rmqs/logs
chmod -R 777 ./rmqs/store
chmod -R 777 ./rmq/logs
chmod -R 777 ./rmq/store

# 下载并启动容器，且为 后台 自动启动
docker-compose up -d

# 查看正在运行中的容器
docker-compose ps

# 显示 rocketmq 容器
docker ps | grep rocketmq

# 停止所有up命令启动的容器
# docker-compose down
