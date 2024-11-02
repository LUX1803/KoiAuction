import { Gender } from "./gender";

export enum Role {
   BIDDER = 'BIDDER',
   BREEDER = 'BREEDER',
   STAFF = 'STAFF',
   MANAGER = 'MANAGER',
   ADMIN = 'ADMIN',
   SHIPPER = 'SHIPPER'
}

export interface Member {
   id: number;
   username: string;
   email: string;
   phone: string;
   role: Role;
   firstname: string;
   lastname: string;
   address: string;
   gender: Gender
}

export interface Farm {
   id: number;
   name: string;
   email: string;
   owner: Member;
}

export interface Employee {
   eeid: string;
   member: Member;
}