
export interface Invoice {
   id: number;
   status: string;
   hammerPrice: number;
   shippingCost: number;
   buyerPremium: number;
   tax: number;
   buyerTotal: number;
   description: string;
   created: Date;
   recipientId: number;
   lotId: number;
}