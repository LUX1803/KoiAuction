
export interface Lot {
   auctionId: number;
   lotId: number;
   started: Date;
   ended: Date;
   startingPrice: number;
   reservePrice: number;
   priceInterval: number;
   estimatedPrice: number;
   buyNowPrice: number;
   buyerPremium: number;
   sellerCommission: number;
   timeInterval: number;

   status: string;
   koiId: number;
   methodId: number;
   staffId: string;
}