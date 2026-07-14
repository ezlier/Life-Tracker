# WebSocket 专注在线检测接入

## 服务端端点

1. 客户端携带已有 JWT 请求 `POST /ws-ticket`。
2. 响应中的 `ticket` 有效期默认为 30 秒，且只能使用一次。
3. 连接 `ws://<host>/ws/presence?ticket=<ticket>`。HTTPS 环境应使用 `wss://`。

`/ws/presence` 由一次性 ticket 完成握手认证，不直接接受查询参数中的长期 JWT。

## 消息协议

连接建立后，客户端绑定当前正在运行的专注记录：

```json
{
  "type": "BIND_FOCUS",
  "focusId": 42,
  "clientId": "stable-browser-installation-uuid"
}
```

每 10 秒发送一次应用心跳：

```json
{
  "type": "HEARTBEAT",
  "focusId": 42,
  "seq": 1,
  "clientTime": 1783930000000
}
```

不再监控该专注时可以发送：

```json
{
  "type": "UNBIND_FOCUS"
}
```

服务端还会每 10 秒发送协议级 Ping，浏览器会自动回复 Pong。这不依赖后台页的 JavaScript 定时器。

## 前端参考代码

```javascript
async function connectPresence(jwt, focusId) {
  const ticketResponse = await fetch('/ws-ticket', {
    method: 'POST',
    headers: { Authorization: `Bearer ${jwt}` }
  });
  const ticketResult = await ticketResponse.json();
  const ticket = ticketResult.data.ticket;
  const protocol = location.protocol === 'https:' ? 'wss:' : 'ws:';
  const ws = new WebSocket(
    `${protocol}//${location.host}/ws/presence?ticket=${encodeURIComponent(ticket)}`
  );

  const clientId = getOrCreateStableClientId();
  let seq = 0;
  let heartbeatTimer;

  ws.addEventListener('open', () => {
    ws.send(JSON.stringify({ type: 'BIND_FOCUS', focusId, clientId }));
    heartbeatTimer = setInterval(() => {
      if (ws.readyState === WebSocket.OPEN) {
        ws.send(JSON.stringify({
          type: 'HEARTBEAT',
          focusId,
          seq: ++seq,
          clientTime: Date.now()
        }));
      }
    }, 10_000);
  });

  ws.addEventListener('close', () => clearInterval(heartbeatTimer));
  return ws;
}
```

客户端断线后应当使用新 ticket 重连并重新发送 `BIND_FOCUS`。如果在离线宽限期结束前恢复，不会自动暂停。

## 默认判定参数

| 参数 | 默认值 |
|---|---:|
| 服务端 Ping 间隔 | 10 秒 |
| 心跳失效时间 | 25 秒 |
| 离线宽限期 | 15 秒 |
| 超时扫描间隔 | 5 秒 |
| 服务重启恢复窗口 | 45 秒 |

参数均可在 `application.yaml` 的 `presence` 节点下调整。

## 离线行为

- 同一 `focusId` 只要还有一个健康连接，就不会暂停。
- 最后一个连接关闭后，保留 15 秒重连宽限期。
- 心跳消失时，通常在约 40–45 秒后暂停，具体受扫描间隔影响。
- 暂停使用带 `user_id` 和 `status = 'running'` 条件的原子 SQL，重复触发不会重复累加时长。
- 自动暂停后不会自动恢复，客户端应刷新专注状态并由用户手动恢复。

当部署多个应用实例时，需要将内存在线注册表和一次性 ticket 迁移到 Redis，并对离线暂停任务加分布式锁。
