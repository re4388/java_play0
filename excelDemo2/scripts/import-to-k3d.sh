#!/bin/zsh
set -euo pipefail

IMAGE_NAME="${1:-exceldemo2:local}"
CLUSTER_NAME="${2:-k3d-default}"

k3d image import "$IMAGE_NAME" -c "$CLUSTER_NAME"