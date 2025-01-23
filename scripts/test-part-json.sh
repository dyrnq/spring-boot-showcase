#!/usr/bin/env bash






for port in 14581 14582 14583; do
  file=/tmp/random_file_$port
  dd if=/dev/urandom of=$file bs=512k count=1
  sha256sum $file | awk '{ print $1 }'

encoding="binary"
#encoding="base64"


boundary="------------------------01acac3b74cbc13c"
CRLF="\r\n"
body="--${boundary}${CRLF}"
body="${body}Content-Disposition: form-data; name=\"json\"${CRLF}"
body="${body}Content-Type: application/json; charset=UTF-8${CRLF}"
body="${body}Content-Transfer-Encoding: 8bit${CRLF}${CRLF}"
body="${body}{\"token\":\"my_token中文\",\"sign\":\"my_sign\" }${CRLF}"
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
http://127.0.0.1:${port}/upload-part-json \
-H "Content-Type: multipart/form-data; boundary=${boundary}" \
--data-binary @/tmp/body.http.$port

done
