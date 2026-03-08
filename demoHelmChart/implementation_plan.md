# Implementation Plan

[Overview]
為 `excelDemo2` 建立
1. 可在 k3d 環境部署的 Helm chart
2. Docker 建置/匯入流程
3. 以及 CI/CD 範本
讓此 Spring Boot 後端服務能從原始碼一路被建置、封裝、部署並透過 Ingress 與 Istio VirtualService 對外提供服務。

目前 `excelDemo2` 是一個獨立的 Spring Boot 4.0.2 / Java 17 Maven 專案，提供 `/api/excel/export` REST API，預設埠號沿用 Spring Boot 預設的 `8080`，專案內尚未存在任何 Dockerfile、Helm chart、Kubernetes manifest、CI workflow 或部署腳本。這表示部署能力需要從零補齊，且必須避免破壞現有的 library/service 雙模式設計與既有 E2E 測試結構。

本次實作的範圍應聚焦在「部署工作流產品化」：第一層是建立可重現的容器映像建置方式，第二層是建立可參數化的 Helm chart，第三層是補上 k3d 本機開發部署流程，第四層是加入 CI/CD 範本，讓後續可以延伸到遠端 registry 與實際叢集。由於使用者指定 k3d、Docker build 與 image import 流程、並要求透過 Ingress 與 Istio VirtualService 對外暴露，因此規劃上需同時支援標準 Kubernetes Service/Ingress 與 Istio 資源，且預設值需偏向本機可操作、最少外部依賴。

[Types]
本次主要新增部署設定型別，而非修改 Java 業務領域型別。

Helm values 結構應完整定義以下設定節點：

- `image`
  - `repository: string`：Docker image 名稱，例如 `exceldemo2`
  - `tag: string`：映像版本，預設可為 `latest` 或 chart appVersion
  - `pullPolicy: string`：例如 `IfNotPresent`
- `service`
  - `type: string`：預設 `ClusterIP`
  - `port: int`：對外 service port，預設 `8080`
  - `targetPort: int|string`：容器 port 名稱或數值，預設 `8080`
- `containerPort: int`：Deployment 容器暴露埠號，預設 `8080`
- `resources`
  - `requests.cpu: string`
  - `requests.memory: string`
  - `limits.cpu: string`
  - `limits.memory: string`
- `ingress`
  - `enabled: boolean`
  - `className: string`：k3d 常見可搭配 nginx ingress
  - `annotations: map<string,string>`
  - `hosts: list<object>`：每個 host 包含 `host: string` 與 `paths: list<object>`，每個 path 至少含 `path: string`、`pathType: string`
  - `tls: list<object>`：可先保留空陣列
- `virtualService`
  - `enabled: boolean`
  - `gateway: string`：Istio Gateway 名稱，預設可指向 `istio-system/ingressgateway` 或自建 gateway 名稱
  - `hosts: list<string>`
  - `http: list<object>`：每筆規則至少包含 `match.prefix: string`、`rewrite.uri: string?`、`timeout: string?`
- `autoscaling`
  - `enabled: boolean`
  - `minReplicas: int`
  - `maxReplicas: int`
  - `targetCPUUtilizationPercentage: int`
- `env`
  - `list<object>` 或 `map<string,string>`：注入容器環境變數
- `health`
  - `liveness.path: string`
  - `readiness.path: string`
  - `initialDelaySeconds: int`
  - `periodSeconds: int`
  - 若不修改應用程式加入 actuator，則需規劃使用 TCP probe 或根據實際可用路徑改為 `/api/excel/export` 不適合 GET，因此較佳方案是在應用新增健康檢查端點或引入 actuator。
- `k3d`
  - `clusterName: string`
  - `namespace: string`
  - `imageTarball: string` 或 `importImageName: string`：供本機腳本使用

CI/CD 設定文件中的變數型別亦需明確化：

- `IMAGE_NAME: string`
- `IMAGE_TAG: string`
- `K3D_CLUSTER: string`
- `K8S_NAMESPACE: string`
- `HELM_RELEASE: string`
- `CHART_PATH: string`
- `INGRESS_HOST: string`

[Files]
本次將新增部署與交付相關檔案，並可能小幅修改應用配置檔與 README。

詳細規劃如下：

- 新增檔案
  - `excelDemo2/Dockerfile`
    - 建立 Spring Boot fat jar 容器映像，建議使用 multi-stage build：builder 階段執行 Maven package，runtime 階段使用 JRE 17 映像執行 `*-exec.jar`
  - `excelDemo2/.dockerignore`
    - 排除 `target/`, `.git/`, `.idea/`, `*.iml` 等，縮小 build context
  - `excelDemo2/helm/exceldemo2/Chart.yaml`
    - Helm chart metadata，定義 chart 名稱、版本、appVersion
  - `excelDemo2/helm/exceldemo2/values.yaml`
    - 預設部署值，包含 image/service/ingress/virtualService/resources/autoscaling 等
  - `excelDemo2/helm/exceldemo2/values-k3d.yaml`
    - k3d 在地開發覆寫值，例如 host、gateway、是否啟用 ingress/virtualService、image tag
  - `excelDemo2/helm/exceldemo2/templates/_helpers.tpl`
    - 共用名稱、labels、selector labels helper
  - `excelDemo2/helm/exceldemo2/templates/deployment.yaml`
    - Spring Boot 容器 Deployment
  - `excelDemo2/helm/exceldemo2/templates/service.yaml`
    - ClusterIP Service
  - `excelDemo2/helm/exceldemo2/templates/ingress.yaml`
    - Kubernetes Ingress，受 `ingress.enabled` 控制
  - `excelDemo2/helm/exceldemo2/templates/virtualservice.yaml`
    - Istio VirtualService，受 `virtualService.enabled` 控制
  - `excelDemo2/helm/exceldemo2/templates/hpa.yaml`
    - HPA，受 `autoscaling.enabled` 控制
  - `excelDemo2/helm/exceldemo2/templates/serviceaccount.yaml`
    - 若不需要特殊權限可選擇建立或明確關閉，仍建議預留模板
  - `excelDemo2/helm/exceldemo2/templates/NOTES.txt`
    - 安裝後提示如何用 `kubectl port-forward`、host 設定與 curl 測試
  - `excelDemo2/scripts/build-image.sh`
    - 本機 Docker build 腳本，負責建出 image
  - `excelDemo2/scripts/import-to-k3d.sh`
    - 將 image 匯入指定 k3d cluster
  - `excelDemo2/scripts/deploy-k3d.sh`
    - 串接 build/import/helm upgrade --install 的一鍵部署腳本
  - `excelDemo2/scripts/uninstall-k3d.sh`
    - 清理 release 用腳本
  - `excelDemo2/.github/workflows/ci.yml`
    - CI 範本：build、test、package、docker build、helm lint/template 驗證
  - `excelDemo2/.github/workflows/cd-k3d-template.yml` 或 `excelDemo2/.github/workflows/cd.yml`
    - CD 範本：示範如何部署到目標叢集；若僅作範本可註記 secrets/runner 前提
  - `excelDemo2/docs/deployment-k3d.md`
    - 詳列 k3d + ingress + istio 前置條件、指令與驗證方式

- 既有檔案修改
  - `excelDemo2/pom.xml`
    - 視最終健康檢查策略，加入 `spring-boot-starter-actuator`
    - 視 Docker build 方式，可能補強 `finalName`、`repackage` 或 profile 說明，但盡量不改現有 library/fat jar 策略
  - `excelDemo2/src/main/resources/application.properties`
    導入 actuator，加入 `management.endpoints.web.exposure.include=health,info` 與 health probe 相關設定
  - `excelDemo2/README.md`
    - 補充 Docker、Helm、k3d、本機部署與 CI/CD 說明

- 不預期刪除或搬移檔案
  - 現有 Java source、測試與 auto-configuration 檔案應保持原位置

[Functions]
本次主要新增 shell 腳本流程與可能的應用健康檢查功能，Java 端函式變更預計很少。

詳細規劃如下：

- 新增函式 / 命令流程（Shell script 層級）
  - `build_image()` / 腳本主流程（`excelDemo2/scripts/build-image.sh`）
    - 目的：接收 image name/tag，執行 Docker build
  - `import_image_to_k3d()` / 腳本主流程（`excelDemo2/scripts/import-to-k3d.sh`）
    - 目的：將本機 image 匯入 k3d cluster
  - `deploy_chart()` / 腳本主流程（`excelDemo2/scripts/deploy-k3d.sh`）
    - 目的：執行 `helm upgrade --install`
  - `cleanup_release()` / 腳本主流程（`excelDemo2/scripts/uninstall-k3d.sh`）
    - 目的：刪除 helm release 與選擇性提示資源清理

- 修改函式（Java）
  - `ExcelDemo2Application.main(String[] args)`
    - 預期不需修改；僅在健康檢查策略要求時保留原狀
  - `ExcelController.exportExcel(...)`
    - 預期不需修改，除非後續要補充 observability/logging 標記
  - 若新增健康檢查 controller，則新增例如 `HealthController.health()` 於 `excelDemo2/src/main/java/com/ben/exceldemo2/controller/HealthController.java`
    - 目的：提供簡單 GET `/healthz` 或 `/readyz` 給 K8s probe 使用

- 移除函式
  - 無規劃移除既有 Java 方法

[Classes]
本次的 Java class 變更應以最小侵入為原則，優先新增部署所需非 Java 檔案；若需要 probe endpoint，再最小量新增 class。

詳細規劃如下：

- 新增類別（視健康檢查策略）
  - `com.ben.exceldemo2.controller.HealthController`
    - 檔案：`excelDemo2/src/main/java/com/ben/exceldemo2/controller/HealthController.java`
    - 關鍵方法：`health()`、必要時 `ready()`
    - 繼承：無
    - 用途：提供 Kubernetes readiness/liveness probe 的 HTTP 200 端點

- 修改類別
  - `com.ben.exceldemo2.ExcelDemo2Application`
    - 可能無需修改
  - `com.ben.exceldemo2.controller.ExcelController`
    - 可能無需修改
  - `com.ben.exceldemo2.service.ExcelService`
    - 不應受部署變更影響

- 移除類別
  - 無

[Dependencies]
本次依賴異動以部署與可觀測性支援為主。

- Java/Maven 依賴
  - 新增 `org.springframework.boot:spring-boot-starter-actuator`
    - 用於健康檢查與後續 metrics 擴充

- 工具依賴（文件與腳本假設）
  - Docker
  - helm
  - kubectl
  - k3d
  - Istio CLI / 既有 Istio 安裝（至少文件中需列為前置條件）
  - Ingress controller（k3d 常用 nginx ingress；文件需明示）

- CI/CD 平台依賴
  - GitHub Actions runner 環境需有 Java 17、Maven、Docker、Helm
  - 若未實際接遠端 registry，CD workflow 應標示為 template，需要使用者補上 secrets 與 kubeconfig

[Implementation Order]
先補齊應用容器化與健康檢查，再建立 Helm chart 與 k3d 腳本，最後補文件與 CI/CD 範本，以降低部署除錯成本並確保每層都有可驗證輸出。

1. 確認健康檢查方案：採用 actuator，並完成應用最小變更。
2. 建立 `Dockerfile` 與 `.dockerignore`，確保能穩定產生可執行 image，並使用 `*-exec.jar` 啟動。
3. 建立 Helm chart 基礎結構：`Chart.yaml`、`values.yaml`、`_helpers.tpl`。
4. 完成 `deployment.yaml`、`service.yaml`，讓應用在 K8s 內可基本運作。
5. 加入 `ingress.yaml` 與 `virtualservice.yaml`，以 values 控制是否啟用與路由規則。
6. 視需要補上 `serviceaccount.yaml`、`hpa.yaml`、`NOTES.txt`，完善 chart 使用體驗。
7. 建立 k3d 專用腳本：build image、import image、deploy/uninstall release。
8. 撰寫 `values-k3d.yaml` 與 `docs/deployment-k3d.md`，整理 k3d + Istio + Ingress 的前置安裝與操作步驟。
9. 更新 `README.md`，加入容器化、Helm、k3d、驗證與故障排查說明。
10. 建立 GitHub Actions CI workflow：Maven test、Docker build、Helm lint/template。
11. 建立 GitHub Actions CD template：示範如何套用 chart 與部署 image。
12. 驗證本機流程：至少以 `helm template`、`helm lint`、Docker build、k3d import 指令路徑完成驗證。

task_progress Items:
- [ ] Step 1: 補齊健康檢查策略與應用最小必要設定
- [ ] Step 2: 建立 Dockerfile 與 .dockerignore 完成容器化
- [ ] Step 3: 建立 Helm chart 基礎結構與核心 Deployment/Service 模板
- [ ] Step 4: 加入 Ingress、Istio VirtualService、HPA 與 k3d values 設定
- [ ] Step 5: 建立 k3d 本機部署腳本（build/import/deploy/uninstall）
- [x] Step 6: 撰寫部署文件與 README 更新
- [x] Step 7: 建立 GitHub Actions CI/CD 範本並完成模板驗證
