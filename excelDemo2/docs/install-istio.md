# 在 k3d 安裝 Istio 與 Ingress Gateway 教學

這份文件說明如何在 **k3d** 環境中安裝 **Istio Service Mesh** 及其 **Ingress Gateway**。

Istio Ingress Gateway 負責接收叢集外部的流量，並根據 `VirtualService` 的規則將流量導向內部的服務。這與你專案中 `excelDemo2/helm/exceldemo2/templates/virtualservice.yaml` 的設計息息相關。

---

## 1. 為什麼要裝 Istio？

如果你在 Helm chart 中啟用了 `virtualService.enabled: true`，Kubernetes 需要 Istio 控制平面（Control Plane）來解析這些自定義資源（CRD）。

- **Ingress Gateway**：充當叢集邊緣的 Load Balancer。
- **VirtualService**：定義路由邏輯（例如：超時、重試、權重分流）。

---

## 2. 安裝 Istio CLI (istioctl)

在安裝 Istio 前，建議先在開發機安裝 `istioctl` 工具。

### macOS (使用 Homebrew)
```bash
brew install istioctl
```

### Linux / 其他
```bash
curl -L https://istio.io/downloadIstio | sh -
cd istio-*
export PATH=$PWD/bin:$PATH
```

驗證安裝：
```bash
istioctl version
```

---

## 3. 在 k3d 安裝 Istio

我們使用 Istio 官方推薦的 `demo` profile 進行安裝，這會包含 `istiod` (控制平面) 與 `istio-ingressgateway`。

### 步驟 1：預檢叢集
```bash
istioctl x precheck
```

### 步驟 2：執行安裝
```bash
istioctl install --set profile=demo -y
```

### 步驟 3：確認元件狀態
```bash
kubectl get pods -n istio-system
kubectl get svc -n istio-system
```
你應該會看到 `istio-ingressgateway` 正處於 `Running` 且獲得了一個外部 IP（在 k3d 中通常會對應到節點 IP）。

---

## 4. 啟用命名空間自動注入 (Sidecar Injection)

為了讓 Istio 能管理 `excelDemo2` 的流量，通常建議開啟自動注入 Sidecar Proxy：

```bash
kubectl create namespace exceldemo2 --dry-run=client -o yaml | kubectl apply -f -
kubectl label namespace exceldemo2 istio-injection=enabled
```

這樣之後部署到 `exceldemo2` 命名空間的 Pod 就會自動帶有一個 `istio-proxy` 容器。

---

## 5. 驗證 Gateway 與 VirtualService

當你執行專案的部署腳本後：
```bash
./scripts/deploy-k3d.sh exceldemo2 local k3d-default exceldemo2 exceldemo2
```

你可以檢查 VirtualService 是否正確綁定到 Gateway：

```bash
kubectl get vs -n exceldemo2
kubectl describe vs exceldemo2-exceldemo2 -n exceldemo2
```

### 測試流量 (透過 Gateway)

1. 找出 Gateway 的外部埠號（如果不是 80）：
```bash
kubectl get svc istio-ingressgateway -n istio-system
```

2. 使用 curl 模擬經由 Gateway 進來的請求：
```bash
curl -i -H "Host: exceldemo2.local" http://<GATEWAY_IP>/actuator/health/readiness
```

---

## 6. 常見問題

### Q: 為什麼我的 VirtualService 沒反應？
- 檢查 `gateways` 欄位是否指向正確的名稱（預設為 `istio-system/istio-ingressgateway`）。
- 檢查 `hosts` 是否與請求的 HTTP Header 一致。

### Q: k3d 的 LoadBalancer 沒給 IP？
- 在 k3d 建立叢集時，若沒指定 `-p` 映射，可能需要透過 `kubectl port-forward` 打開入口：
```bash
kubectl port-forward deployment/istio-ingressgateway -n istio-system 8080:80
```

---

## 7. 移除 Istio

如果需要清理環境：
```bash
istioctl x uninstall --purge -y
kubectl delete namespace istio-system
```
