export interface Transaction {
   id: number;
   amount: number;
   description: string;
   paymentType: string;
   status: string;
   created: Date;
   updated: Date;
   closed: Date;
   memberId: number;
   invoiceId: number;
   paymentId: number;
}