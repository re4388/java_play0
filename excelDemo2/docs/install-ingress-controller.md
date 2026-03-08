# 在 k3d 安裝 Ingress Controller 教學

這份文件會帶你在 **k3d / Kubernetes** 環境中安裝 **Ingress Controller**，並說明如何讓目前的 `excelDemo2` Helm chart 可以正常透過 Ingress 對外提供服務。

本文以 **ingress-nginx** 為主，因為它是目前最常見、文件最完整、也最適合搭配 k3d 使用的 Ingress Controller。

---

## 1. 什麼是 Ingress Controller？

Kubernetes 的 `Ingress` 資源本身只是一份「路由規則宣告」，它不會自己處理 HTTP/HTTPS 流量。

要讓這些規則真正生效，你需要安裝一個 **Ingress Controller**，它會：

- 監聽叢集裡的 `Ingress` 資源
- 根據 host/path 規則轉送流量
- 將外部請求導向對應的 Kubernetes `Service`

如果你只有建立 Ingress YAML，但沒有安裝 Ingress Controller，那麼：

- `kubectl get ingress` 雖然看得到資源
- 但實際上外部流量不會被處理
- 你的 `excelDemo2` 也不會真的從 host/path 被打到

---

## 2. 為什麼在 k3d 建議用 ingress-nginx？

在 k3d 中，`ingress-nginx` 是最常見的選擇，原因有：

- 安裝簡單
- 社群文件多
- 能直接對應你目前 Helm chart 裡的設定：

```yaml
ingress:
  enabled: true
  className: nginx
```

也就是說，你目前 `excelDemo2/helm/exceldemo2/values.yaml` 與 `values-k3d.yaml` 已經是朝 **nginx ingress** 的方向設計。

---

## 3. 前置需求

在開始之前，請確認你本機已經有：

- Docker
- k3d
- kubectl
- helm

確認版本：

```bash
docker --version
k3d version
kubectl version --client
helm version
```

---

## 4. 建立適合 Ingress 的 k3d cluster

如果你還沒建立 k3d cluster，建議直接建立一個有 **80 / 443 port mapping** 的 cluster，這樣未來從本機打 Ingress 會比較直觀。

```bash
k3d cluster create demo \
  --agents 1 \
  -p "80:80@loadbalancer" \
  -p "443:443@loadbalancer"
```

說明：

- `-p "80:80@loadbalancer"`
  - 把你電腦的 80 port 對到 k3d loadbalancer 的 80
- `-p "443:443@loadbalancer"`
  - 把你電腦的 443 port 對到 k3d loadbalancer 的 443

建立後確認：

```bash
k3d cluster list
kubectl cluster-info
kubectl get nodes
```

如果你已經有既有 cluster，也可以繼續使用，但若沒有對應 80/443 的 mapping，之後你可能需要改用 `kubectl port-forward` 或重建 cluster。

---

## 5. 安裝 ingress-nginx：推薦用 Helm

這是最推薦的方法，因為：

- 升級方便
- 版本管理清楚
- 比較容易查詢 chart 設定

### 步驟 1：加入 Helm repo

```bash
helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx
helm repo update
```

### 步驟 2：建立 namespace

```bash
kubectl create namespace ingress-nginx
```

### 步驟 3：安裝 ingress-nginx

```bash
helm upgrade --install ingress-nginx ingress-nginx/ingress-nginx \
  --namespace ingress-nginx \
  --set controller.publishService.enabled=true
```

### 步驟 4：確認安裝狀態

```bash
kubectl get pods -n ingress-nginx
kubectl get svc -n ingress-nginx
kubectl get ingressclass
```

你應該至少會看到：

- 一個 `ingress-nginx-controller` Pod
- 一個 `ingress-nginx-controller` Service
- 一個 `nginx` 的 `IngressClass`

---

## 6. 安裝 ingress-nginx：替代方案，用官方 manifest

如果你不想用 Helm，也可以直接套用官方 manifest。

```bash
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/main/deploy/static/provider/cloud/deploy.yaml
```

接著等待 Pod Ready：

```bash
kubectl wait --namespace ingress-nginx \
  --for=condition=ready pod \
  --selector=app.kubernetes.io/component=controller \
  --timeout=180s
```

驗證：

```bash
kubectl get pods -n ingress-nginx
kubectl get svc -n ingress-nginx
kubectl get ingressclass
```

> 如果沒有特別需求，還是建議優先使用 Helm 安裝方式。

---

## 7. 驗證 Ingress Controller 是否正常

安裝完成後，請先確認 controller 真正有起來。

### 檢查 Pod

```bash
kubectl get pods -n ingress-nginx -o wide
```

你要看到 Pod 狀態是：

- `Running`
- `READY 1/1` 或相近狀態

### 檢查 Service

```bash
kubectl get svc -n ingress-nginx
```

### 檢查 IngressClass

```bash
kubectl get ingressclass
```

你應該會看到類似：

```text
nginx   k8s.io/ingress-nginx
```

這代表你的 `ingressClassName: nginx` 有對應到實際 controller。

---

## 8. 怎麼跟 excelDemo2 串起來？

你現在專案中的 Helm values 已經有這段：

```yaml
ingress:
  enabled: true
  className: nginx
  hosts:
    - host: exceldemo2.local
      paths:
        - path: /
          pathType: Prefix
```

這代表只要 Ingress Controller 裝好，之後部署 `excelDemo2` 時，Kubernetes 會建立一個 `Ingress`，並交給 `nginx` controller 處理。

部署服務：

```bash
cd excelDemo2
./scripts/deploy-k3d.sh exceldemo2 local k3d-default exceldemo2 exceldemo2
```

部署完確認：

```bash
kubectl get all -n exceldemo2
kubectl get ingress -n exceldemo2
kubectl describe ingress -n exceldemo2
```

---

## 9. 設定本機 hosts

因為你的 Ingress host 預設是：

```text
exceldemo2.local
```

所以你需要在本機 `/etc/hosts` 加上一筆對應。

### 先找 k3d load balancer IP

```bash
kubectl get svc -n ingress-nginx
docker ps --format 'table {{.Names}}\t{{.Ports}}'
```

如果你是用：

```bash
k3d cluster create demo -p "80:80@loadbalancer" -p "443:443@loadbalancer"
```

通常你可以直接把 host 指到 `127.0.0.1`：

```text
127.0.0.1 exceldemo2.local
```

然後加入：

```bash
sudo sh -c 'echo "127.0.0.1 exceldemo2.local" >> /etc/hosts'
```

---

## 10. 測試 Ingress 是否真的可用

確認服務與 ingress 都存在：

```bash
kubectl get svc -n exceldemo2
kubectl get ingress -n exceldemo2
```

然後用 curl 測試：

```bash
curl -H "Host: exceldemo2.local" http://127.0.0.1/actuator/health/readiness
```

如果 `/etc/hosts` 已設定好，也可以直接：

```bash
curl http://exceldemo2.local/actuator/health/readiness
```

預期應該回傳類似：

```json
{"status":"UP"}
```

---

## 11. 常見問題排查

### 問題 1：`kubectl get ingress` 有資源，但打不到服務

先檢查：

```bash
kubectl get ingress -n exceldemo2
kubectl describe ingress -n exceldemo2
kubectl get pods -n ingress-nginx
```

常見原因：

- Ingress Controller 沒安裝
- Ingress Controller Pod 沒 Ready
- `ingressClassName` 不對
- host 沒設到 `/etc/hosts`

---

### 問題 2：`IngressClass nginx not found`

表示 `ingress-nginx` 還沒正確安裝，或 controller 尚未建立 `IngressClass`。

重新確認：

```bash
kubectl get ingressclass
```

---

### 問題 3：Controller Pod 一直起不來

看 log：

```bash
kubectl logs -n ingress-nginx deploy/ingress-nginx-controller
kubectl describe pod -n ingress-nginx
```

常見原因：

- 叢集資源不足
- image 拉不到
- RBAC / admission webhook 初始化失敗

---

### 問題 4：本機打 `exceldemo2.local` 解析不到

檢查：

```bash
cat /etc/hosts | grep exceldemo2.local
ping exceldemo2.local
```

如果沒有設定，就補上：

```text
127.0.0.1 exceldemo2.local
```

---

### 問題 5：你同時用了 Ingress 與 Istio VirtualService，怎麼分工？

你目前專案是兩者都支援：

- `Ingress`：由 ingress-nginx 處理 HTTP 入口
- `VirtualService`：由 Istio 做 service mesh / gateway routing

這兩種路由能力可以並存，但你要清楚知道：

- 如果你從 nginx ingress 進來，主要是 Kubernetes ingress 規則在工作
- 如果你從 Istio gateway 進來，主要是 Istio VirtualService 在工作

建議本機先把 **Ingress Controller 路徑打通**，再進一步驗證 Istio。

---

## 12. 建議的最短成功路徑

如果你想最快把流程跑通，建議照這個順序：

1. 建立 k3d cluster（帶 80/443 port mapping）
2. 用 Helm 安裝 `ingress-nginx`
3. 確認 `kubectl get ingressclass` 有 `nginx`
4. 部署 `excelDemo2`
5. 設定 `/etc/hosts`
6. `curl http://exceldemo2.local/actuator/health/readiness`

---

## 13. 快速指令總整理

### 建 cluster

```bash
k3d cluster create demo \
  --agents 1 \
  -p "80:80@loadbalancer" \
  -p "443:443@loadbalancer"
```

### 安裝 ingress-nginx

```bash
helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx
helm repo update
kubectl create namespace ingress-nginx
helm upgrade --install ingress-nginx ingress-nginx/ingress-nginx \
  --namespace ingress-nginx \
  --set controller.publishService.enabled=true
```

### 驗證

```bash
kubectl get pods -n ingress-nginx
kubectl get svc -n ingress-nginx
kubectl get ingressclass
```

### 部署 excelDemo2

```bash
cd excelDemo2
./scripts/deploy-k3d.sh exceldemo2 local k3d-default exceldemo2 exceldemo2
kubectl get ingress -n exceldemo2
```

### 測試

```bash
curl -H "Host: exceldemo2.local" http://127.0.0.1/actuator/health/readiness
```
