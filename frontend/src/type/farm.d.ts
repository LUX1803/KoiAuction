import { Member } from "./member";

export enum FarmStatus {
   PENDING,
   APPROVED,
   REJECTED
}

export interface Farm {
   id: number;
   name: string;
   description: string;
   status: FarmStatus;
   image: string
   sent: Date;
   replied: Date;
   owner: Member
   staff: E
}