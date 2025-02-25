#!/usr/bin/env bash




## test forward-headers-strategy
for port in 24581 24582 24583; do

name=nginx-$port
upstream_port=$((port-10000));

docker rm -f $name 2>/dev/null 1>/dev/null || true


cat >$HOME/nginx-$port.conf<<EOF

upstream backend {
   #least_conn;
   server host.docker.internal:$upstream_port;
#   keepalive 100;
   keepalive 32;
   keepalive_requests 1000;
   keepalive_time 1h;
   keepalive_timeout 120s;
}

server {
listen $port reuseport backlog=8192 so_keepalive=10m:30s:10;

keepalive_requests 30000;
keepalive_time 30m;
keepalive_timeout 150s;

#net.ipv4.tcp_keepalive_time = 600
#net.ipv4.tcp_keepalive_intvl = 30
#net.ipv4.tcp_keepalive_probes = 5


# [so_keepalive=on|off|[keepidle]:[keepintvl]:[keepcnt]];

#    real_ip_header X-Forwarded-For;
#    real_ip_recursive off;
#    set_real_ip_from  0.0.0.0/0;
#    set_real_ip_from  ::/0;

location / {
  root /usr/share/nginx/html;
}

location /api/ {
  rewrite ^/api/(.*)$ /\$1 break;
  proxy_pass http://backend;
  proxy_http_version 1.1;
  proxy_set_header Connection "";
  proxy_set_header Host \$host;
  proxy_set_header X-Real-IP \$remote_addr;
  proxy_set_header X-Forwarded-For \$proxy_add_x_forwarded_for;
  proxy_set_header X-Forwarded-Proto \$scheme;
  proxy_set_header X-Forwarded-Port  \$server_port;
  proxy_set_header X-Forwarded-Host  \$host;
  proxy_hide_header X-Protocol-Hide;
  proxy_pass_header Server;
  proxy_busy_buffers_size 512k;
  proxy_buffers 8 512k;
  proxy_buffer_size 256k;
  proxy_read_timeout 1800;
  proxy_connect_timeout 1800;
  proxy_send_timeout 1800;
  client_max_body_size 50M;
  proxy_next_upstream error timeout invalid_header http_500 http_502 http_503 http_504;
  proxy_ssl_protocols TLSv1 TLSv1.1 TLSv1.2 TLSv1.3;
  proxy_ssl_ciphers HIGH:!aNULL:!MD5;
  proxy_ssl_verify off;
  proxy_set_header cookie \$http_cookie;
  chunked_transfer_encoding off;
  port_in_redirect off;
  absolute_redirect off;
  proxy_socket_keepalive on;
}
}
EOF

  docker \
  run -d \
  --name $name \
  --restart always \
  --add-host=host.docker.internal:host-gateway \
  -p $port:$port \
  -v ./html:/usr/share/nginx/html \
  -v $HOME/nginx-$port.conf:/etc/nginx/conf.d/default.conf \
  nginx
done

sleep 5s;

for port in 24581 24582 24583; do

  echo "###############################################"$port
  curl -fiSL \
    -H "Content-Type: application/json" \
    -H "X-Forwarded-For: 203.0.113.195" \
    -H "X-Forwarded-Proto: http" \
    -H "X-Forwarded-Host: example.com" \
    http://127.0.0.1:${port}/api/ip
  echo ""

  echo "************"
  curl -fiSL http://127.0.0.1:${port}/api/ip
  echo ""

done

