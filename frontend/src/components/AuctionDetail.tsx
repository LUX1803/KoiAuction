import { getAuctionById } from '@/service/auctionService';
import { Auction, AuctionStatus } from '@/type/auction'
import { formatDate } from 'date-fns';
import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom'
import LotList from './LotList';

const AuctionDetail = () => {
   const { auctionId } = useParams();

   const [auction, setAuction] = useState<Auction | null>();

   useEffect(() => {

      getAuctionById(auctionId)
         .then((data) => {
            setAuction(data);
         })
         .catch((error) => {
            console.error(error);
         });

   }, []);

   return (
      <>
         {auction ? (
            <>
               <h2 className='text-2xl font-bold'>Auction #{auctionId}</h2>
               <div className='text-md mt-1'>{formatDate(auction.started, "dd.MM.yyyy HH:mm a")} - {formatDate(auction.ended, "dd.MM.yyyy HH:mm a")}</div>

               <LotList auctionId={auction.id} />
            </>
         ) : (
            <p>No Auction Found</p>
         )}
      </>

   )
}

export default AuctionDetail