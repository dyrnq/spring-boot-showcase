#!/usr/bin/env bash

for port in 14581 14582 14583; do
echo ""
echo "==================>${port}"
curl -fsS -H "Accept-Encoding:gzip" http://127.0.0.1:${port}/hello/2047

echo ""
echo "==================>${port}"
curl -fsS -H "Accept-Encoding:gzip" http://127.0.0.1:${port}/hello/2048 | gzip --decompress

echo ""
echo "==================>${port}"
curl -fsS -H "Accept-Encoding:gzip" http://127.0.0.1:${port}/hello/2049 | gzip --decompress

done