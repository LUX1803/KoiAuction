import { Auction } from '@/type/auction'
import { formatInTimeZone } from 'date-fns-tz';

function AuctionItem({ auction }: { auction: Auction }) {
   const timezone = 'Asia/Bangkok';

   return (
      <div className="flex flex-col p-4 border border-gray-200 rounded-2xl">
         <h2 className="text-lg font-bold">Auction #{auction.id}</h2>
         <p className="text-sm text-gray-500">{formatInTimeZone(new Date(auction.started), timezone, 'dd.MM.yyyy HH:mm a')} - {formatInTimeZone(new Date(auction.ended), timezone, 'dd.MM.yyyy HH:mm a')}</p>
      </div>
   )
}

export default AuctionItem