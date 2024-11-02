
export enum AuctionStatus {
   PENDING = "PENDING",
   ONGOING = "ONGOING",
   UPCOMING = "UPCOMING",
   CLOSED = "CLOSED",
   CANCELED = "CANCELED"
}

export interface Auction {
   id: number;
   name: string;
   description: string;
   started: Date;
   ended: Date;
   signup_opened: Date;
   signup_closed: Date;
   signup_fee: number;
   status: AuctionStatus;
   created: Date;
   updated: Date;
   eeid: string;
}