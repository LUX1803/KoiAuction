import { getBidByLotIdAndBidderId, placeDutchBid } from '@/service/bidService';
import { useEffect } from 'react'
import { LotDetailProps } from './LotDetail';

const PlaceDutchBid = (lotDetail: LotDetailProps) => {
   const { user } = useAuth();
   const [isPlacedBid, setIsPlacedBid] = useState<boolean>(false);

   useEffect(() => {
      getBidByLotIdAndBidderId(lotDetail.lotId, user.id).then((data) => {
         if (data) {
            setIsPlacedBid(true);
         }
      }).catch((error) => {
         console.error(error);
      })
   }, [])

   function placeBid() {
      placeDutchBid(lotDetail.lotId).then((data) => {
         console.log(data);
      }).catch((error) => {
         console.error(error);
      })
   }
   return (
      <>
         {isPlacedBid ? (
            <div className="text-2xl font-semibold">You have already placed a bid</div>
         ) : (
            <div className="border-2 border-gray-200 rounded-lg p-2">
               <button onClick={placeBid} className="bg-blue-500 text-white p-2 rounded-lg">Buy now: {lotDetail.buyNowPrice}</button>
            </div>
         )}
      </>
   )
}

export default PlaceDutchBid