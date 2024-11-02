import { getAuctionByStatus } from "@/service/auctionService";
import { Auction, AuctionStatus } from "@/type/auction.d";
import { useEffect, useState } from "react";
import { NavLink } from "react-router-dom";
import Countdown from 'react-countdown';
import { formatInTimeZone } from "date-fns-tz";

const AuctionEvent = () => {

   const [auction, setAuction] = useState<Auction | null>();

   useEffect(() => {
      // Fetch the ongoing auction
      getAuctionByStatus(AuctionStatus.ONGOING)
         .then((data) => {
            if (data) {
               setAuction(data[0]);
            } else {
               // No ongoing auction, fetch upcoming
               fetchUpcomingAuction();
            }
         })
         .catch((error) => {
            // Check if the error is a 404 Not Found
            if (error.response && error.response.status === 404) {
               fetchUpcomingAuction(); // If no ongoing auction, try to fetch upcoming
            } else {
               console.error("Error fetching ongoing auction:", error);
            }
         });
   }, []);

   // Fetch upcoming auction if no ongoing auction is found
   const fetchUpcomingAuction = () => {
      getAuctionByStatus(AuctionStatus.UPCOMING)
         .then((data) => {
            setAuction(data); // Set upcoming auction if available
         })
         .catch((error) => {
            console.error("Error fetching upcoming auction:", error);
         });
   };

   const renderer = ({ days, hours, minutes, seconds, completed }) => {
      if (completed) {
         return <span>Auction Ended!</span>;
      } else {
         return (
            <div>
               <span>{days}d {hours}h {minutes}m {seconds}s</span>
            </div>
         );
      }
   };


   return (
      <div className="flex flex-col justify-center items-center py-20">
         {auction ? (
            <div className="flex flex-col justify-center items-center w-72 h-72 bg-[#4685AF] text-white rounded-full shadow-lg mb-6 relative">
               <h2 className="text-2xl font-bold mb-2">Auction #{auction.id}</h2>
               <Countdown className="text-lg font-light mb-4" date={auction.status === AuctionStatus.ONGOING ? auction.ended : auction.started} renderer={renderer} />
               {auction.status === 'ONGOING' && (<NavLink
                  to={`/auction/${auction.id}`}
                  className="hover:bg-gray-500 text-white font-bold py-2 px-6 rounded-full absolute bottom-6">
                  ENTER
               </NavLink>)}

            </div>
         ) : (
            <p className="mt-20 text-3xl text-stone-400 font-bold mb-8">No Scheduled Auctions</p>
         )
         }
         <div className="!mt-10">
            <NavLink
               to="/auction/past"
               className="bg-slate-500 w-40 h-10 text-white flex justify-center items-center text-wrap rounded-full">
               View past auctions
            </NavLink>
         </div>
      </div >
   )
};

export default AuctionEvent;
