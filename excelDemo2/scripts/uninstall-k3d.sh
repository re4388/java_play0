#!/bin/zsh
set -euo pipefail

RELEASE_NAME="${1:-exceldemo2}"
NAMESPACE="${2:-exceldemo2}"

helm uninstall "$RELEASE_NAME" -n "$NAMESPACE"