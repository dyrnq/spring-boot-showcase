#!/usr/bin/env bash



for port in 14581 14582 14583; do
  file=/tmp/random_file_$port
  dd if=/dev/urandom of=$file bs=1M count=1
  sha256sum $file | awk '{ print $1 }'
  curl \
    -F "token=my_token" \
    -F "sign=my_sign" \
    -F "file=@${file}" \
    http://127.0.0.1:${port}/upload-part \
    --header "Content-Type: multipart/form-data"

    echo ""
done



for port in 14581 14582 14583; do
  file=/tmp/random_file_$port
  dd if=/dev/urandom of=$file bs=1024k count=1
  sha256sum $file | awk '{ print $1 }'

fileBase64=$(base64 -w0 -i $file)
#fileBase64=$(cat $file)
#备注，此处不能直接使用cat,可能文件中包含一些特殊的字符，需要特殊处理，但是暂时没有找到合适的方法
boundary="------------------------01acac3b74cbc13c"

body="--$boundary\r\n"
body="${body}Content-Disposition: form-data; name=\"token\"\r\n\r\n"
body="${body}my_token\r\n"
body="${body}--$boundary\r\n"
body="${body}Content-Disposition: form-data; name=\"sign\"\r\n\r\n"
body="${body}my_sign\r\n"
body="${body}--$boundary\r\n"
body="${body}Content-Disposition: form-data; name=\"file\"; filename=\"random_file_$port\"\r\n"
body="${body}Content-Transfer-Encoding: base64\r\n"
body="${body}Content-Type: application/octet-stream\r\n\r\n"
body="${body}${fileBase64}\r\n"
body="${body}--$boundary--\r\n"
echo -e "${body}" > /tmp/body.http.$port
curl -X POST \
http://127.0.0.1:${port}/upload-part \
-H "Content-Type: multipart/form-data; boundary=$boundary" \
--data-binary @/tmp/body.http.$port

done

## https://technodoggo.hashnode.dev/send-a-multipartrelated-request-with-curl