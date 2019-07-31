# cachedb
A Java based All-In-Cache DB, Read-Cache, Write-DB-First, Single node with multi CPUs, NOT support Cluster

基于 java 的全量缓存数据库，读缓存，写DB，单点部署，可主备部署，不支持集群。

## 应用场景

常见中小系统通常读并发 200QPS 以下， 事务并发 20TPS 以下， 数据量级千万级以下。

目前常规服务器硬件配置通常在 16CPU, 16G内存以上，单节点服务已经可以支持较高的并发。

实际应用中微服务集群方案成本较高，且效果有限。

因此有了我们这个方案。

我们不追新，我们的目标是易用且够用。

## 架构图

![image](/out/doc/puml/architect/架构图.png)

## 方案对比

|特性|mysql,ehcache,groovy|redis,lua|mongodb|mysql|
|---|---|---|---|---|
|单点事务特性|好|无|差|好|
|持久化|好|差|好|好|
|快速复杂查询|好|好|中|差|
|性能水平扩展|无|中|好|中|
