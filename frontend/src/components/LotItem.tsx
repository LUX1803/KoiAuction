
import { NavLink } from "react-router-dom";
import Countdown, { zeroPad } from "react-countdown";
import { useEffect, useState } from "react";
import { LotItemProps } from "@/type/lotItem";
import { Stomp } from "@stomp/stompjs";
import SockJS from "sockjs-client";
import { formatMoney } from "@/util/helper";



const LotItem = (lot: LotItemProps) => {

   const [lotItem, setLotItem] = useState<LotItemProps>(lot)

   const { auctionId, lotId, ended, startingPrice, status, image, variety, currentBid, started, methodId } = lotItem;


   useEffect(() => {

      if (status == "LIVE" || status == "UPCOMING") {
         const socket = new SockJS("http://localhost:8080/ws");
         const client = Stomp.over(socket);

         client.connect({}, () => {
            console.log("Connected to WebSocket");

            client.subscribe(`/topic/lot/${lotId}`, (message) => {
               const data = JSON.parse(message.body);
               setLotItem(data); // Update the lot detail
               console.log(data);
            });
         });


         return () => {
            client.disconnect();
         };
      }
   }, [auctionId, lotId]);





   const renderer = ({ days, hours, minutes, seconds, completed }) => {
      if (completed) {
         return <></>;
      } else {
         return (
            <div className="flex text-black">
               {/* <div className="text-lg font-bold">{(days)}d
                  <span className="px-2">:</span>
               </div> */}
               <div className="text-lg font-bold">{(hours + days * 24)}h
                  <span className="px-2">:</span>
               </div>
               <div className="text-lg font-bold">{zeroPad(minutes)}m
                  <span className="px-2">:</span>
               </div>
               <div className="text-lg font-bold">{zeroPad(seconds)}s</div>
            </div>
         );
      }
   };



   return (

      <div className="block relative">
         <NavLink
            to={`/auction/${auctionId}/${lotId}`}
            className="">

            <span className="flex items-center font-semibold text-lg gap-2 ml-2 absolute text-black top-4 left-4">
               <svg xmlns="http://www.w3.org/2000/svg" aria-hidden="true" role="img" width="1em" height="1em" viewBox="0 0 24 24" ><path fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 11.172V5a2 2 0 0 1 2-2h6.172a2 2 0 0 1 1.414.586l8 8a2 2 0 0 1 0 2.828l-6.172 6.172a2 2 0 0 1-2.828 0l-8-8A2 2 0 0 1 3 11.172M7 7h.001"></path>
               </svg>
               {lotId}
            </span>

            <div className="flex justify-center bg-[#E8F0FE] rounded-t-2xl">
               <img className="transform scale-90 object-contain aspect-[2/3]  rounded-lg  relative rounded-t-2xl h-5/6" loading="lazy" src={image} alt='Koi' />
            </div>

            <div className="bg-[#76bace5d] rounded-b-2xl px-4 py-4">
               <span className="text-2xl font-semibold">    {variety}
               </span>


               <div className="flex justify-between mt-4">
                  {
                     status && (
                        <>
                           <div>
                              <div>
                                 <svg
                                    className={`w-6 h-6 mr-2 inline-block pb-1 ${(status == "LIVE" || status == "UPCOMING" ? "text-[#FFA500]" : "")} `}
                                    xmlns="http://www.w3.org/2000/svg" aria-hidden="true" role="img" viewBox="0 0 24 24"><path fill="currentColor" fill-rule="evenodd" d="M2 12C2 6.477 6.477 2 12 2s10 4.477 10 10s-4.477 10-10 10S2 17.523 2 12m11-5a1 1 0 1 0-2 0v3.764a3 3 0 0 0 1.658 2.683l2.895 1.447a1 1 0 1 0 .894-1.788l-2.894-1.448a1 1 0 0 1-.553-.894z" clip-rule="evenodd"></path>
                                 </svg>
                                 <span className="text-lg font-semibold">{status}</span>
                              </div>
                              {
                                 status == 'UPCOMING' ? (
                                    <Countdown
                                       key={status}
                                       className="text-lg font-light mb-4"
                                       date={started}
                                       daysInHours
                                       renderer={renderer} />
                                 ) : (
                                    <Countdown
                                       key={status}
                                       className="text-lg font-light mb-4"
                                       date={ended}
                                       daysInHours
                                       renderer={renderer} />
                                 )
                              }

                           </div>

                           <div className="">
                              {
                                 (currentBid) ? (
                                    <>
                                       <span className="text-lg font-semibold bg-green-400 text-white px-2 py-1 rounded-full">
                                          <svg
                                             className="mr-2 p-0 transition-transform animate-pulse -rotate-90 iconify iconify--majesticons inline w-6 h-6 mb-1"
                                             xmlns="http://www.w3.org/2000/svg" aria-hidden="true" role="img" width="1em" height="1em" viewBox="0 0 24 24"><path fill="currentColor" fill-rule="evenodd" d="M1 12C1 5.925 5.925 1 12 1s11 4.925 11 11s-4.925 11-11 11S1 18.075 1 12m8.528-4.882a1 1 0 0 1 1.027.05l6 4a1 1 0 0 1 0 1.664l-6 4A1 1 0 0 1 9 16V8a1 1 0 0 1 .528-.882" clip-rule="evenodd"></path>
                                          </svg>
                                          {
                                             (methodId == 2 && status == "LIVE") ? (
                                                <>
                                                   ₫{formatMoney(startingPrice)}
                                                </>) : (
                                                <>
                                                   ₫{formatMoney(currentBid)}
                                                </>)

                                          }

                                       </span>
                                    </>
                                 ) : (
                                    <>
                                       <span className="text-lg font-semibold bg-gray-400 text-black px-2 py-1 mt-4 rounded-full">
                                          <svg
                                             className="mr-2 p-0 transition-transform true iconify iconify--majesticons inline w-6 h-6 pb-1"
                                             xmlns="http://www.w3.org/2000/svg" aria-hidden="true" role="img" viewBox="0 0 24 24"><path fill="currentColor" fill-rule="evenodd" d="M1 12C1 5.925 5.925 1 12 1s11 4.925 11 11s-4.925 11-11 11S1 18.075 1 12m8.528-4.882a1 1 0 0 1 1.027.05l6 4a1 1 0 0 1 0 1.664l-6 4A1 1 0 0 1 9 16V8a1 1 0 0 1 .528-.882" clip-rule="evenodd"></path>
                                          </svg>
                                          ₫{formatMoney(startingPrice)}
                                       </span>
                                    </>
                                 )
                              }

                           </div>
                        </>
                     )
                  }
               </div>
            </div>
            {/* <CardFooter className="grid grid-cols-2 gap-4">
               <div className="">
                  <div className="flex items-center justify-center space-x-2">
                     <div className="w-5 h-5 text-blue-500">
                        <svg xmlns="http://www.w3.org/2000/svg" aria-hidden="true" role="img" className="mr-2 iconify iconify--majesticons" width="100%" height="100%" viewBox="0 0 24 24"><g fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"><circle cx="12" cy="12" r="9"></circle><path d="M8 4v2a2 2 0 0 0 2 2h1a2 2 0 0 1 2 2v0a2 2 0 0 0 2 2v0a2 2 0 0 0 2-2v0a2 2 0 0 1 2-2h1m0 8h-3a2 2 0 0 0-2 2v2m-4 0v-2a2 2 0 0 0-2-2v0a2 2 0 0 1-2-2v0a2 2 0 0 0-2-2H3"></path></g></svg>
                     </div>
                     <div>
                        <div className="text-gray-400 text-sm">Breeder</div>
                        <div className="font-semibold text-gray-900">{breeder}</div>
                     </div>
                  </div>
                  <div className="flex items-center space-x-2 justify-center">
                     <div className="w-5 h-5 text-blue-500">
                        <svg xmlns="http://www.w3.org/2000/svg" aria-hidden="true" role="img" className="mr-2 iconify iconify--majesticons" width="100%" height="100%" viewBox="0 0 24 24"><path fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="m6 12l-1.586 1.586a2 2 0 0 0 0 2.828l3.172 3.172a2 2 0 0 0 2.828 0l9.172-9.172a2 2 0 0 0 0-2.828l-3.172-3.172a2 2 0 0 0-2.828 0L12 6m-6 6l2 2m-2-2l3-3m3-3l2 2m-2-2L9 9m0 0l3 3"></path></svg>
                     </div>
                     <div>
                        <div className="text-gray-400 text-sm">Length</div>
                        <div className="font-semibold text-gray-900">{length} cm</div>
                     </div>
                  </div>
               </div>
               <div>
                  <div className="flex items-center space-x-2 justify-center">
                     <div className="w-5 h-5 text-blue-500">
                        <svg xmlns="http://www.w3.org/2000/svg" aria-hidden="true" role="img" className="mr-2 iconify iconify--mingcute" width="100%" height="100%" viewBox="0 0 24 24"><g fill="none" fill-rule="evenodd"><path d="m12.593 23.258l-.011.002l-.071.035l-.02.004l-.014-.004l-.071-.035q-.016-.005-.024.005l-.004.01l-.017.428l.005.02l.01.013l.104.074l.015.004l.012-.004l.104-.074l.012-.016l.004-.017l-.017-.427q-.004-.016-.017-.018m.265-.113l-.013.002l-.185.093l-.01.01l-.003.011l.018.43l.005.012l.008.007l.201.093q.019.005.029-.008l.004-.014l-.034-.614q-.005-.018-.02-.022m-.715.002a.02.02 0 0 0-.027.006l-.006.014l-.034.614q.001.018.017.024l.015-.002l.201-.093l.01-.008l.004-.011l.017-.43l-.003-.012l-.01-.01z"></path><path fill="currentColor" d="M7 9.5a7.5 7.5 0 1 1 2.942 5.957l-1.788 1.787L9.58 18.67a1 1 0 1 1-1.414 1.414L6.74 18.659l-2.12 2.12a1 1 0 0 1-1.414-1.415l2.12-2.12l-1.403-1.403a1 1 0 1 1 1.414-1.414L6.74 15.83l1.79-1.79A7.47 7.47 0 0 1 7 9.5M14.5 4a5.5 5.5 0 1 0 0 11a5.5 5.5 0 0 0 0-11"></path></g></svg>
                     </div>
                     <div>
                        <div className="text-gray-400 text-sm">Gender</div>
                        <div className="font-semibold text-gray-900">{gender.substring(0, 1) + gender.substring(1).toLocaleLowerCase()}</div>
                     </div>
                  </div>
                  <div className="flex items-center space-x-2 justify-center">
                     <div className="w-5 h-5 text-blue-500">
                        <svg xmlns="http://www.w3.org/2000/svg" aria-hidden="true" role="img" className="mr-2 iconify iconify--majesticons" width="100%" height="100%" viewBox="0 0 24 24"><g fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"><path d="M7 9a4 4 0 0 0-4 4v6a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-6a4 4 0 0 0-4-4v0M7 9h10M7 9V6m10 3V6m-5 0v3m0-6h.01M7 3h.01M17 3h.01"></path><path d="M3 13c0 1 .6 3 3 3s3-2 3-3c0 1 .6 3 3 3s3-2 3-3c0 1 .6 3 3 3s3-2 3-3"></path></g></svg>
                     </div>
                     <div>
                        <div className="text-gray-400 text-sm">Born in</div>
                        <div className="font-semibold text-gray-900">{yob}</div>
                     </div>
                  </div>
               </div>
            </CardFooter> */}
         </NavLink>
      </div>

   );
};

export default LotItem