# excelDemo2 k3d 部署指南

## 前置需求

- Docker
- k3d
- kubectl
- helm
- Istio
- 一個可用的 Ingress Controller（例如 nginx ingress）

## 建議流程

1. 建立或確認 k3d cluster。
2. 安裝 Ingress Controller。
3. 安裝 Istio 與 ingress gateway。
4. 執行：

```bash
./scripts/deploy-k3d.sh exceldemo2 local k3d-default exceldemo2 exceldemo2
```

5. 驗證：

```bash
kubectl get all -n exceldemo2
helm list -n exceldemo2
kubectl get ingress,virtualservice -n exceldemo2
kubectl port-forward svc/exceldemo2-exceldemo2 8080:8080 -n exceldemo2
curl http://127.0.0.1:8080/actuator/health/readiness
```

## hosts 設定

若要使用 `exceldemo2.local`，請在本機 `/etc/hosts` 加入對應到 k3d load balancer 的 IP。

## 移除部署

```bash
./scripts/uninstall-k3d.sh exceldemo2 exceldemo2
```