import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

const socketUrl = 'http://localhost:8080/ws'; // Backend WebSocket URL

export const connectWebSocket = (onMessageReceived) => {
   const stompClient = new Client({
      webSocketFactory: () => new SockJS(socketUrl),
      onConnect: () => {
         console.log('Connected to WebSocket');
         stompClient.subscribe('/topic/auction', (message) => {
            onMessageReceived(JSON.parse(message.body));
         });
      },
      onStompError: (frame) => {
         console.error('Error with WebSocket', frame);
      },
   });
   stompClient.activate();

   return stompClient;
};
