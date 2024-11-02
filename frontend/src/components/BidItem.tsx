import { useAuth } from "@/context/AuthContext";
import { Bid } from "@/type/bid"
import { formatMoney } from "@/util/helper"
import { formatDate } from "date-fns"


const BidItem = ({ bid, color, bigNum, fp }: { bid: Bid, color: string, bigNum: string, fp: boolean }) => {
   const { user } = useAuth();
   return (
      <>
         {
            fp == true ? (
               <>
                  <div className={`flex justify-between mt-2 rounded-2xl bg-gray-300 px-4 py-2 ${user?.id == bid.bidderId ? 'bg-green-400 text-white text-xl font-semibold' : 'bg-gray-300'}`}>
                     <div>
                        <div className={`${user?.id == bid.bidderId ? 'text-3xl font-bold' : 'text-2xl font-semibold'} pb-2`}>₫
                           {formatMoney(bid.amount)}</div>
                        <div>{formatDate(bid.time, "dd.MM.yyyy, HH:mm a")}</div>
                     </div>
                     <div>{bid.bidderFirstname + ' ' + bid.bidderLastname}</div>
                  </div>
               </>
            ) : (
               <>
                  <div className={`flex justify-between mt-2 rounded-2xl bg-gray-300 px-4 py-2 ${color}`}>
                     <div>
                        <div className={`${bigNum} pb-2`}>₫
                           {formatMoney(bid.amount)}</div>
                        <div>{formatDate(bid.time, "dd.MM.yyyy, HH:mm a")}</div>
                     </div>
                     <div>{bid.bidderFirstname + ' ' + bid.bidderLastname}</div>

                  </div>
               </>
            )
         }
      </>

   )
}

export default BidItem