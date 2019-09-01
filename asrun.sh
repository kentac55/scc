#!/bin/sh

cat > a.s

echo '------ a.s ------'

cat a.s

echo '------ result ------'

gcc a.s

./a.out

echo $?
