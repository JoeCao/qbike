#!/usr/bin/env bash
if [ ! -f "zipkin.jar" ]; then
  curl -sSL https://zipkin.io/quickstart.sh | bash -s
fi
RABBIT_ADDRESSES=localhost java -jar zipkin.jar