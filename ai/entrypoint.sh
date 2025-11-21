#!/bin/bash

if [ -n "$JUPYTER_PASSWORD" ]; then
  export JUPYTER_PASSWORD_HASH=$(python3 - <<EOF
from jupyter_server.auth import passwd
print(passwd("$JUPYTER_PASSWORD"))
EOF
)
fi

exec jupyter lab --ip=0.0.0.0 --no-browser --allow-root \
  --IdentityProvider.hashed_password="$JUPYTER_PASSWORD_HASH"
