import { useEffect, useState } from 'react';
import { Bid } from '@/type/bid';
import SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';
import BidItem from './BidItem';
import { getBidListByLotId } from '@/service/bidService';
import { useAuth } from '@/context/AuthContext';
import { get } from 'http';

const BidList = ({ lotId, lotStatus, methodId }: { lotId: string, lotStatus: string, methodId: number }) => {
   const [bids, setBids] = useState<Bid[]>([]);
   const { user } = useAuth();

   useEffect(() => {
      getBidListByLotId(lotId).then(setBids).catch(console.error);
   }, [lotId]);

   useEffect(() => {
      if (lotStatus === 'LIVE') {
         const socket = new SockJS("http://localhost:8080/ws");
         const client = Stomp.over(socket);

         client.connect({}, () => {
            console.log("Connected to WebSocket for bid list");
            client.subscribe(`/topic/lot/${lotId}/bid`, (message) => {
               const data = JSON.parse(message.body);
               setBids((prevBids) => [...prevBids, data]);
            });
         });

         return () => client.disconnect();
      }
   }, [lotStatus, lotId]);

   const highestBid = bids.slice().sort((a, b) => b.amount - a.amount)[0];
   const userBid = bids.find((bid) => bid.bidderId === user.id);

   const isFixedPriceAuction = methodId === 1;
   const isSealedBidAuction = methodId === 2;

   if (methodId == 1 && lotStatus == 'AWARDED') {

      return (
         <>
            {bids
               .sort((a, b) => new Date(b.time).getTime() - new Date(a.time).getTime())
               .map((bid) => (
                  <BidItem
                     key={bid.id}
                     bid={bid}
                     color={
                        (bid.highest == true)
                           ? 'bg-green-400 text-white text-xl font-semibold' : 'bg-gray-300'
                     }
                     bigNum={bid.highest == true ? 'text-3xl font-bold' : 'text-2xl font-semibold'}
                     fp={false}
                  />
               ))}
         </>
      )
   }

   return (
      <>
         {(methodId != 2) ? (
            bids.length > 0 ? (
               <div>
                  {bids
                     .sort((a, b) => new Date(b.time).getTime() - new Date(a.time).getTime())
                     .map((bid, index) => (
                        <BidItem
                           key={bid.id}
                           bid={bid}
                           color={
                              index === 0 && !isSealedBidAuction
                                 ? 'bg-green-400 text-white text-xl font-semibold'
                                 : bid.bidderId === user.id
                                    ? 'bg-gray-200' // Highlight user's bid in fixed price auction
                                    : 'bg-gray-300'
                           }
                           bigNum={index === 0 ? 'text-3xl font-bold' : 'text-2xl font-semibold'}
                           fp={isFixedPriceAuction && lotStatus === 'LIVE'}
                        />
                     ))}
               </div>
            ) : (
               <div className="text-2xl font-semibold">No bids yet</div>
            )
         ) : (
            <>
               {lotStatus === 'LIVE' && (
                  <>{userBid ? <BidItem bid={userBid} color="bg-green-400 text-white text-xl font-semibold" bigNum="text-3xl font-bold" fp={false} /> : <div className="text-2xl font-semibold">Place bid now</div>}</>
               )}
               {lotStatus === 'AWARDED' && (
                  <>
                     {highestBid && <BidItem bid={highestBid} color="bg-green-400 text-white text-xl font-semibold" bigNum="text-3xl font-bold" fp={false} />}
                     {userBid && userBid !== highestBid && <BidItem bid={userBid} color="bg-gray-300" bigNum="" fp={false} />}
                  </>
               )}
            </>
         )}
      </>
   );
};

export default BidList;
