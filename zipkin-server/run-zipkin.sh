#!/usr/bin/env bash
curl -sSL https://zipkin.io/quickstart.sh | bash -s
RABBIT_ADDRESSES=localhost java -jar zipkin.jar