#!/bin/bash

if [ "$#" -lt 2 ] ; then
  echo
  echo "Usage: $0 <local_ip> <seed_ip>" >&2
  exit 1
fi

find .. -name "*.conf" | xargs perl -p -i.bak -e "s/hostname\s*=\s*.127.0.0.1./hostname=\"$1\"/gc"
find .. -name "*.conf" | xargs perl -p -i.bak -e "s/ClusterSystem\@127.0.0.1/ClusterSystem\@$2/gc"


