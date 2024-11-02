import { Stomp } from "@stomp/stompjs";
import { useEffect, useRef, useState } from "react";
import SockJS from "sockjs-client";


const useWebSocket = (auctionId: number, lotId: number) => {
   const [lotDetail, setLotDetail] = useState(null);
   const [bidList, setBidList] = useState([]);
   const [highestBid, setHighestBid] = useState(null);
   const stompClient = useRef(null);

   useEffect(() => {
      const connectWebSocket = () => {
         const socket = new SockJS("http://localhost:8080/ws");
         const client = Stomp.over(socket);

         client.connect({}, () => {
            console.log("Connected to WebSocket");

            client.subscribe(`/topic/auction/${auctionId}/${lotId}`, (message) => {
               const data = JSON.parse(message.body);
               setLotDetail(data); // Update the lot detail
               console.log(data);
            });
         }, (error) => {
            console.error("Error connecting to WebSocket:", error);
            setTimeout(() => {
               client.connect({}, () => {
                  console.log("Connected to WebSocket");

                  client.subscribe(`/topic/auction/${auctionId}/${lotId}`, (message) => {
                     const data = JSON.parse(message.body);
                     setLotDetail(data); // Update the lot detail
                     console.log(data);
                  });
               });
            }, 5000);
         });


         return () => {
            client.disconnect();
         };
      };

      const placeBid = (bidAmount) => {
         const bidData = {
            lotId,
            amount: bidAmount,
         };
         if (stompClient.current) {
            stompClient.current.send(`/app/lot/${lotId}/english-bid`, {}, JSON.stringify(bidData));
         }
      };

      return { lotDetail, bidList, highestBid, placeBid };
   };

   export default useWebSocket;
