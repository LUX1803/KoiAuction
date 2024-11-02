
export interface LotItemProps {
   auctionId: number;
   lotId: number;
   started: Date;
   ended: Date;
   startingPrice: number;
   status: string;
   image: string;
   breeder: string;
   variety: string;
   currentBid: number;
}