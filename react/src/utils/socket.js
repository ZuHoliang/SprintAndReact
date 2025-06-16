import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";

let client = null;

//建立 STOMP WebSocket 連線
export const connectSocket = (userId, onMessage) => {
  client = new Client({
    // 使用 SockJS 包裝 WebSocket 連線
    webSocketFactory: () => new SockJS("http://localhost:8088/ws"),
    //每5秒連線一次
    reconnectDelay: 5000,
    //成功連線後
    onConnect: () => {
      //指定訂閱主題
      client.subscribe(`/topic/notifications/${userId}`, (msg) => {
        try {
          const body = JSON.parse(msg.body);
          if (onMessage) onMessage(body);
        } catch {
          console.error("訊息解析失敗", err);
        }
      });
    },
  });
  client.activate();
};

//中斷WebSocket連線
export const disconnectSocket = () => {
  if (client) {
    client.deactivate();
    client = null;
  }
};
