#!/usr/bin/env bash

ver=${ver:-11.1.0}

target_dir="/usr/local/bin/"

if [ -w "$target_dir" ]; then
  :
else
  target_dir="$HOME/mitmproxy"
fi
mkdir -p ${target_dir}

if [ ! -f ${target_dir}/mitmweb ]; then
  curl -fSL -# https://downloads.mitmproxy.org/${ver}/mitmproxy-${ver}-linux-x86_64.tar.gz | tar -xvz -C ${target_dir}
fi


ls -l ${target_dir}
echo "${target_dir}/mitmweb -p 5588 --mode "reverse:http://127.0.0.1:14582" --set web_port=8083"