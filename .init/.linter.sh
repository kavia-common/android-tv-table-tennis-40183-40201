#!/bin/bash
cd /home/kavia/workspace/code-generation/android-tv-table-tennis-40183-40201/ping_pong_frontend
./gradlew lint
LINT_EXIT_CODE=$?
if [ $LINT_EXIT_CODE -ne 0 ]; then
   exit 1
fi

