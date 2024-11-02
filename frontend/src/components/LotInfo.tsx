import { formatMoney } from '@/util/helper'
import { LotDetailProps } from './LotDetail'
import { useAuth } from '@/context/AuthContext'

const LotInfo = (lotDetail: LotDetailProps) => {
   const { user } = useAuth();

   switch (lotDetail.methodId) {
      case 1:
         return (<>
            <div className="text-left right-20 ">
               <div className=" text-center text-xl font-semibold"> <div>{lotDetail.methodName} Auction</div>
               </div>
               <div className="flex justify-between">
                  <div>Starting Price</div>
                  <div>₫{formatMoney(lotDetail.startingPrice)}</div>
               </div>
               <div className="flex justify-between">
                  <div>Buyer Premium</div>
                  <div>{lotDetail.buyerPremium * 100} %</div>
               </div>
               {
                  user?.role == 'BREEDER' && (
                     <>
                        <div className="flex justify-between">
                           <div>Seller Commision</div>
                           <div>{lotDetail.sellerCommission * 100} %</div>
                        </div>
                     </>)
               }
            </div>
         </>)
      case 2:
         return (
            <div className="text-left right-20 ">
               <div className=" text-center text-xl font-semibold"> <div>{lotDetail.methodName} Auction</div>
               </div>
               <div className="flex justify-between">
                  <div>Starting Price</div>
                  <div>₫{formatMoney(lotDetail.startingPrice)}</div>
               </div>
               <div className="flex justify-between">
                  <div>Estimated Price</div>
                  <div>₫{formatMoney(lotDetail.estimatedPrice)}</div>
               </div>
               <div className="flex justify-between">
                  <div>Buyer Premium</div>
                  <div>{lotDetail.buyerPremium * 100}%</div>
               </div>
               {
                  user?.role == 'BREEDER' && (
                     <>
                        <div className="flex justify-between">
                           <div>Seller Commision</div>
                           <div>{lotDetail.sellerCommission * 100} %</div>
                        </div>
                     </>)
               }
            </div>
         )
      case 3:
         return (<>
            <div className="text-left right-20 ">
               <div className=" text-center text-xl font-semibold"> <div>{lotDetail.methodName} Auction</div>
               </div>
               <div className="flex justify-between">
                  <div>Starting Price</div>
                  <div>₫{formatMoney(lotDetail.startingPrice)}</div>
               </div>
               <div className="flex justify-between">
                  <div>Price Interval</div>
                  <div>₫{formatMoney(lotDetail.priceInterval)}</div>
               </div>
               {/* <div className="flex justify-between">
                  <div>Time Interval</div>
                  <div>{lotDetail.timeInterval.toString().substring(2)}</div>
               </div> */}
               <div className="flex justify-between">
                  <div>Buyer Premium</div>
                  <div>{lotDetail.buyerPremium * 100}%</div>
               </div>
               {
                  user?.role == 'BREEDER' && (
                     <>
                        <div className="flex justify-between">
                           <div>Seller Commision</div>
                           <div>{lotDetail.sellerCommission * 100} %</div>
                        </div>
                     </>)
               }
            </div>
         </>)
      case 4:
         return (<>
            <div className="text-left right-20 ">
               <div className=" text-center text-xl font-semibold"> <div>{lotDetail.methodName} Auction</div>
               </div>
               <div className="flex justify-between">
                  <div>Starting Price</div>
                  <div>₫{formatMoney(lotDetail.startingPrice)}</div>
               </div>
               <div className="flex justify-between">
                  <div>Price Interval</div>
                  <div>₫{formatMoney(lotDetail.priceInterval)}</div>
               </div>
               <div className="flex justify-between">
                  <div>Time Interval</div>
                  <div>{lotDetail.timeInterval.toString().substring(2)}</div>
               </div>
               <div className="flex justify-between">
                  <div>Buyer Premium</div>
                  <div>{lotDetail.buyerPremium * 100}%</div>
               </div>
               {
                  user?.role == 'BREEDER' && (
                     <>
                        <div className="flex justify-between">
                           <div>Seller Commision</div>
                           <div>{lotDetail.sellerCommission * 100} %</div>
                        </div>
                     </>)
               }
            </div>
         </>)
   }
}

export default LotInfo