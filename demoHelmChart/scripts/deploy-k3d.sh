#!/bin/zsh
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
PROJECT_DIR="$(cd "$SCRIPT_DIR/.." && pwd)"

IMAGE_NAME="${1:-exceldemo2}"
IMAGE_TAG="${2:-local}"
CLUSTER_NAME="${3:-k3d-default}"
NAMESPACE="${4:-exceldemo2}"
RELEASE_NAME="${5:-exceldemo2}"

"$SCRIPT_DIR/build-image.sh" "$IMAGE_NAME" "$IMAGE_TAG"
"$SCRIPT_DIR/import-to-k3d.sh" "${IMAGE_NAME}:${IMAGE_TAG}" "$CLUSTER_NAME"

kubectl create namespace "$NAMESPACE" --dry-run=client -o yaml | kubectl apply -f -

helm upgrade --install "$RELEASE_NAME" "$PROJECT_DIR/helm/exceldemo2" \
  --namespace "$NAMESPACE" \
  -f "$PROJECT_DIR/helm/exceldemo2/values.yaml" \
  -f "$PROJECT_DIR/helm/exceldemo2/values-k3d.yaml" \
  --set image.repository="$IMAGE_NAME" \
  --set image.tag="$IMAGE_TAG" \
  --set k3d.clusterName="$CLUSTER_NAME" \
  --set k3d.namespace="$NAMESPACE" \
  --set k3d.releaseName="$RELEASE_NAME"