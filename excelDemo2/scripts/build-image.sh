#!/bin/zsh
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
PROJECT_DIR="$(cd "$SCRIPT_DIR/.." && pwd)"

IMAGE_NAME="${1:-exceldemo2}"
IMAGE_TAG="${2:-local}"

docker build -t "${IMAGE_NAME}:${IMAGE_TAG}" "$PROJECT_DIR"