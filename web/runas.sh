#!/bin/bash

# Add local user
# Either use the LOCAL_USER_ID if passed in at runtime or
# fallback


USER_ID=${LOCAL_USER_ID:-9100}

echo "Starting with UID : $USER_ID"

adduser -s /bin/bash -u $USER_ID -D -h $PAYARA_PATH payara
chown -R payara:payara /opt
export HOME=$PAYARA_PATH

sleep 0.5

exec su-exec payara "$@"
