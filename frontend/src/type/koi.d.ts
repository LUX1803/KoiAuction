import { Gender } from "./gender";

export interface Koi {
   id: number;
   yob: number;
   length: number;
   status: string;
   gender: Gender
   image: string;
   message: string;
   sent: Date;
   replied: Date;
   note: string;
   varietyId: number;
   varietyName: string;
   farmId: number;
   farmName: string;
   staffId: string;
}