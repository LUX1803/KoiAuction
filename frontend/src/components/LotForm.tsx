"use client";
import { useState } from "react";
import { createLot } from "@/service/lotService";
import { Button, Input, Label } from "./ui";
import { updateKoi } from "@/service/koiService";

const AuctionMethods = {
   FIXED_PRICE: "Fixed Price",
   SEALED_BID: "Sealed Bid",
   ENGLISH: "English",
   DUTCH: "Dutch",
};

export default function LotForm({ koiId, koiStatus, onLotCreated }: { koiId: number }) {
   const [formData, setFormData] = useState({
      started: "",
      ended: "",
      buyerPremium: "",
      sellerCommission: "",
      auctionMethod: "",
      startingPrice: "",
      estimatedPrice: "",
      reservePrice: "",
      priceInterval: "",
      buyNowPrice: "",
      timeInterval: "",
      notes: "",
   });

   const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
      const { name, value } = e.target;
      setFormData({
         ...formData,
         [name]: value,
      });
   };

   const convertAuctionMethod = (method: string) => {
      switch (method) {
         case AuctionMethods.FIXED_PRICE:
            return 1;
         case AuctionMethods.SEALED_BID:
            return 2;
         case AuctionMethods.ENGLISH:
            return 3;
         case AuctionMethods.DUTCH:
            return 4;
         default:
            return 0;
      }
   };

   const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
      e.preventDefault();

      const lot = {
         koiId,
         methodId: convertAuctionMethod(formData.auctionMethod),
         startingPrice: parseFloat(formData.startingPrice),
         buyerPremium: parseFloat(formData.buyerPremium) || 0,
         sellerCommission: parseFloat(formData.sellerCommission) || 0,
         estimatedPrice: parseFloat(formData.estimatedPrice) || 0,
         reservePrice: parseFloat(formData.reservePrice) || 0,
         priceInterval: parseFloat(formData.priceInterval) || 0,
         buyNowPrice: parseFloat(formData.buyNowPrice) || 0,
         timeInterval: formData.timeInterval || 'PT30S',
         started: new Date(formData.started),
         ended: new Date(formData.ended),
         note: formData.notes || "",
      };

      console.log("Creating lot:", lot);
      await createLot(lot);
      await updateKoi({ id: koiId, note: lot.note, status: "AUCTIONING" });
      onLotCreated();
   };

   return (
      <form onSubmit={handleSubmit} className="space-y-4 bg-white p-4 rounded-2xl">
         {/* Date Fields */}
         <div className="flex gap-10">
            <div className="flex-1">
               <Label htmlFor="started">Start Date</Label>
               <Input
                  type="datetime-local"
                  name="started"
                  value={formData.started}
                  onChange={handleInputChange}
                  required
                  className="border rounded p-2 w-full"
               />
            </div>

            <div className="flex-1">
               <Label htmlFor="ended">End Date</Label>
               <Input
                  type="datetime-local"
                  name="ended"
                  value={formData.ended}
                  onChange={handleInputChange}
                  required
                  className="border rounded p-2 w-full"
               />
            </div>
         </div>

         {/* Commission and Premium Fields */}
         <div className="flex gap-10">
            <div className="flex-1">
               <Label htmlFor="buyerPremium">Buyer Premium</Label>
               <Input
                  type="number"
                  name="buyerPremium"
                  value={formData.buyerPremium}
                  onChange={handleInputChange}
                  min="0.0"
                  step={0.1}
                  required
                  className="border rounded p-2 w-full"
               />
            </div>

            <div className="flex-1">
               <Label htmlFor="sellerCommission">Seller Commission</Label>
               <Input
                  type="number"
                  name="sellerCommission"
                  value={formData.sellerCommission}
                  onChange={handleInputChange}
                  min="0.0"
                  step={0.1}
                  required
                  className="border rounded p-2 w-full"
               />
            </div>
         </div>

         {/* Auction Method and Starting Price Fields */}
         <div className="flex gap-10">
            <div className="flex-1">
               <Label htmlFor="auctionMethod">Auction Method</Label>
               <select
                  name="auctionMethod"
                  value={formData.auctionMethod}
                  onChange={handleInputChange}
                  className="border rounded p-2 w-full"
               >
                  <option value="">Select Method</option>
                  {Object.values(AuctionMethods).map((method) => (
                     <option key={method} value={method}>
                        {method}
                     </option>
                  ))}
               </select>
            </div>

            <div className="flex-1">
               <Label htmlFor="startingPrice">Starting Price</Label>
               <Input
                  type="number"
                  name="startingPrice"
                  value={formData.startingPrice}
                  onChange={handleInputChange}
                  min="0"
                  required
                  className="border rounded p-2 w-full"
               />
            </div>
         </div>

         {/* Conditionally render fields based on auction method */}
         {formData.auctionMethod === AuctionMethods.SEALED_BID && (
            <div className="flex gap-10">
               <div className="flex-1">
                  <Label htmlFor="estimatedPrice">Estimated Price</Label>
                  <Input
                     type="number"
                     name="estimatedPrice"
                     value={formData.estimatedPrice}
                     onChange={handleInputChange}
                     min="0"
                     className="border rounded p-2 w-full"
                  />
               </div>
            </div>
         )}

         {formData.auctionMethod === AuctionMethods.ENGLISH && (
            <div className="flex gap-10">
               <div className="flex-1">
                  <Label htmlFor="priceInterval">Price Interval</Label>
                  <Input
                     type="number"
                     name="priceInterval"
                     value={formData.priceInterval}
                     onChange={handleInputChange}
                     min="0"
                     className="border rounded p-2 w-full"
                  />
               </div>

               <div className="flex-1">
                  <Label htmlFor="buyNowPrice">Buy Now Price</Label>
                  <Input
                     type="number"
                     name="buyNowPrice"
                     value={formData.buyNowPrice}
                     onChange={handleInputChange}
                     min="0"
                     className="border rounded p-2 w-full"
                  />
               </div>
            </div>
         )}

         {formData.auctionMethod === AuctionMethods.DUTCH && (
            <div className="flex gap-10">
               <div className="flex-1">
                  <Label htmlFor="priceInterval">Price Interval</Label>
                  <Input
                     type="number"
                     name="priceInterval"
                     value={formData.priceInterval}
                     onChange={handleInputChange}
                     min="0"
                     className="border rounded p-2 w-full"
                  />
               </div>

               <div className="flex-1">
                  <Label htmlFor="reservePrice">Reserve Price</Label>
                  <Input
                     type="number"
                     name="reservePrice"
                     value={formData.reservePrice}
                     onChange={handleInputChange}
                     min="0"
                     className="border rounded p-2 w-full"
                  />
               </div>
            </div>
         )}

         {/* Staff Notes Section */}
         {
            koiStatus == 'PENDING' && (
               <>
                  <div>
                     <Label htmlFor="notes">Note</Label>
                     <Input
                        type="text"
                        name="notes"
                        value={formData.notes}
                        onChange={handleInputChange}
                        className="border rounded p-2 w-full"
                        placeholder="Add any notes for staff..."
                     />
                  </div>
               </>)
         }


         <Button className="bg-blue-500 text-white rounded-xl w-full py-2 mt-5" type="submit">
            Create Lot
         </Button>
      </form>
   );
}
