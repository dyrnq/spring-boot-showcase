#!/usr/bin/env bash


for port in 14581 14582 14583; do
  curl -fsSL -H "Content-Type: application/json" http://127.0.0.1:${port}/get
  echo ""
done


for port in 14581 14582 14583; do
curl -fsSL -H "Content-Type: application/json" -X POST http://127.0.0.1:${port}/post \
--data @<(cat<<EOF
{"source": "curl-${port}","data": { "field1": "field1_value", "field2":${port}}}
EOF
)

echo ""

done

