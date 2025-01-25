#!/usr/bin/env bash



for port in 14581 14582 14583; do
  curl -X POST \
    --data "name=中文&age=11&address=地址&phone=136999999&remark=否则" \
    http://127.0.0.1:${port}/user/add
    echo ""
done

for port in 14581 14582 14583; do
  curl -X POST \
    -F "name=中文" \
    -F "age=11" \
    -F "address=地址" \
    -F "phone=136999999" \
    -F "remark=否则" \
    http://127.0.0.1:${port}/user/add
    echo ""
done

for port in 14581 14582 14583; do
curl -X POST \
--header "Content-Type: application/json;charset=UTF-8" \
--data '
{
"name": "中文","address": "地址"
}
' http://127.0.0.1:${port}/user/add-body
echo ""
done