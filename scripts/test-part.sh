#!/usr/bin/env bash



for port in 14581 14582 14583; do
  file=/tmp/random_file_$port
  dd if=/dev/urandom of=$file bs=1M count=5
  curl \
    -F "token=my_token" \
    -F "sign=my_sign" \
    -F "file=@${file}" \
    -i \
    --verbose \
    http://127.0.0.1:${port}/upload-part \
    --header "Content-Type: multipart/form-data"
#    rm -rf $file
    echo ""
done

