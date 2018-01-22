#!/bin/bash

/usr/sbin/sshd -D &

/opt/trade-app/bin/runwebapp.sh
