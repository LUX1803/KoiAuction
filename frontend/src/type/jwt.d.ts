import { JwtPayload } from "jsonwebtoken";

export interface CustomJwtPayload extends JwtPayload {
   firstname: string;
   lastname: string;
}