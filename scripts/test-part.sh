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

encoding="binary"
#encoding="base64"


boundary="------------------------01acac3b74cbc13c"
CRLF="\r\n"
body="--${boundary}${CRLF}"
body="${body}Content-Disposition: form-data; name=\"token\"${CRLF}"
body="${body}Content-Type: text/plain; charset=UTF-8${CRLF}"
body="${body}Content-Transfer-Encoding: 8bit${CRLF}${CRLF}"
body="${body}my_token中文${CRLF}"
body="${body}--${boundary}${CRLF}"
body="${body}Content-Disposition: form-data; name=\"sign\"${CRLF}"
body="${body}Content-Type: text/plain; charset=UTF-8${CRLF}"
body="${body}Content-Transfer-Encoding: 8bit${CRLF}${CRLF}"
body="${body}my_sign${CRLF}"
body="${body}--${boundary}${CRLF}"
body="${body}Content-Disposition: form-data; name=\"file\"; filename=\"random_file_$port\"${CRLF}"
body="${body}Content-Type: application/octet-stream${CRLF}"
body="${body}Content-Transfer-Encoding: ${encoding}${CRLF}${CRLF}"

echo -e -n "${body}" > /tmp/body.http.$port

if [ "${encoding}" = "binary" ]; then
  cat $file >> /tmp/body.http.$port
else
  base64 -w0 -i $file >> /tmp/body.http.$port
fi

(
  echo -e -n "${CRLF}"
  echo -e -n "--${boundary}--"
  echo -e -n "${CRLF}"
)>> /tmp/body.http.$port


curl \
-X POST \
http://127.0.0.1:${port}/upload-part \
-H "Content-Type: multipart/form-data; boundary=${boundary}" \
--data-binary @/tmp/body.http.$port

done

## https://technodoggo.hashnode.dev/send-a-multipartrelated-request-with-curl