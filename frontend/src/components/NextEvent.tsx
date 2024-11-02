import { useState, useEffect, useCallback } from "react"
import { getCurrentAuction, startAuction, stopAuction, updateAuction } from "@/service/auctionService"
import { getLotListByAuctionId, updateLot } from "@/service/lotService"
import { Auction } from "@/type/auction.d"
import { Lot } from "@/type/lot.d"
import { formatInTimeZone } from 'date-fns-tz'
import { format, set } from "date-fns"

const timezone = "Asia/Bangkok" // Timezone +7

const NextEvent = () => {
   const [auction, setAuction] = useState<Auction | null>(null)
   const [lotList, setLotList] = useState<Lot[]>([])
   const [reload, setReload] = useState(false)
   const [modifiedFields, setModifiedFields] = useState<{ [key: string]: boolean }>({})

   // Fetch auction and lot data on load
   useEffect(() => {
      getCurrentAuction()
         .then((data) => {
            setAuction(data)
            return getLotListByAuctionId(data.id)
         })
         .then(setLotList)
         .catch(console.error)
         .finally(() => setReload(false))
   }, [reload])

   // Handle auction form submission
   const handleAuctionUpdate = (e: React.FormEvent<HTMLFormElement>) => {
      e.preventDefault()
      if (auction) {
         const formData = new FormData(e.target as HTMLFormElement)
         const updatedAuction = {
            ...auction,
            started: format(new Date(formData.get("started") as string), "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"),
            ended: format(new Date(formData.get("ended") as string), "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"),
         }

         updateAuction(updatedAuction)
            .then(() => {

               setReload(true)
               setModifiedFields((prev) => {
                  const updatedFields = { ...prev };

                  Object.keys(updatedFields).forEach((key) => {
                     if (key.startsWith(`auction_`)) {
                        delete updatedFields[key];
                     }
                  });

                  return updatedFields;
               });
            })
            .catch((error) => {
               console.error(error)
            })
      }
   }

   const handleStartAuction = (auctionId: number) => {
      startAuction(auctionId)
         .then(() => {
            setReload(true)

         })
         .catch(console.error)
   }

   const handleStopAuction = (auctionId: number) => {
      stopAuction(auctionId)
         .then(() => {
            setReload(true)

         })
         .catch(console.error)
   }

   // Handle each lot form submission separately
   const handleLotUpdate = (lotId: number) => (e: React.FormEvent<HTMLFormElement>) => {
      e.preventDefault()

      const formData = new FormData(e.target as HTMLFormElement)
      const updatedLot: Partial<Lot> = {
         auctionId: auction?.id,
         lotId,
         started: new Date(formData.get("started") as string),
         ended: new Date(formData.get("ended") as string),
         buyerPremium: Number(formData.get("buyerPremium")),
         sellerCommission: Number(formData.get("sellerCommission")),
         startingPrice: Number(formData.get("startingPrice")),
         reservePrice: Number(formData.get("reservePrice")),
         priceInterval: Number(formData.get("priceInterval")),
         estimatedPrice: Number(formData.get("estimatedPrice")),
         buyNowPrice: Number(formData.get("buyNowPrice")),
         methodId: Number(formData.get("method")),
      }

      updateLot(updatedLot)
         .then(() => {

            setReload(true)
            setModifiedFields((prev) => {
               const updatedFields = { ...prev };

               // Clear all fields related to this lotId (e.g., lot_1_started, lot_1_ended)
               Object.keys(updatedFields).forEach((key) => {
                  if (key.startsWith(`lot_${lotId}_`)) {
                     delete updatedFields[key]; // Remove the field from the modified state
                  }
               });

               return updatedFields; // Return the updated object with fields cleared
            });
         })
         .catch(console.error)
   }


   // Handle field modification tracking
   const handleLotFieldChange = (lotId: number, fieldName: string, value: any) => {
      const lot = lotList.find(l => l.lotId === lotId);


      if (!lot) return;

      let isModified = false;

      // Compare based on field type
      switch (fieldName) {
         case 'started':
         case 'ended':
            // Convert lot's start/end to the same format as the input value for comparison
            const originalDate = new Date(lot[fieldName]);
            const formattedOriginalDate = formatInTimeZone(originalDate, timezone, 'yyyy-MM-dd HH:mm');
            value = format(new Date(value), 'yyyy-MM-dd HH:mm');
            console.log('new vaulue', value)
            console.log('old value', formattedOriginalDate)
            isModified = value != formattedOriginalDate;
            break;
         case 'buyerPremium':
         case 'sellerCommission':
         case 'startingPrice':
         case 'reservePrice':
         case 'priceInterval':
         case 'estimatedPrice':
         case 'buyNowPrice':
            isModified = Number(value) !== lot[fieldName];
            break;
         default:
            isModified = value !== lot[fieldName];
            break;
      }

      setModifiedFields((prev) => ({
         ...prev,
         [`lot_${lotId}_${fieldName}`]: isModified
      }));
   };

   const handleAuctionFieldChange = (fieldName: string, value: any) => {
      if (!auction) return;

      let isModified = false;

      // Compare based on field type
      switch (fieldName) {
         case 'started':
         case 'ended':
            // Convert lot's start/end to the same format as the input value for comparison
            const originalDate = new Date(auction[fieldName]);
            const formattedOriginalDate = formatInTimeZone(originalDate, timezone, 'yyyy-MM-dd HH:mm');
            value = format(new Date(value), 'yyyy-MM-dd HH:mm');
            isModified = value != formattedOriginalDate;
            break;
         default:
            isModified = value !== auction[fieldName];
            break;
      }

      setModifiedFields((prev) => ({
         ...prev,
         [`auction_${fieldName}`]: isModified
      }));
   }

   return (
      <div className="p-6 bg-white">
         {auction && (
            <>
               {/* Auction Form */}
               <form className="mb-4 inline-block" onSubmit={handleAuctionUpdate}>
                  <h2 className="text-xl font-bold mb-4">Auction #{auction.id} <span className="ml-10">{auction.status}</span></h2>

                  <div className="flex space-x-4 mb-4 gap-10">
                     <label>
                        <span className="text-2xl">Start: </span>
                        <input
                           type="datetime-local"
                           name="started"
                           defaultValue={formatInTimeZone((auction.started), timezone, "yyyy-MM-dd HH:mm")} // Set to local format

                           onChange={(e) => handleAuctionFieldChange('started', e.target.value)}  // Track changes
                           className={`p-2 border rounded-2xl ml-2 ${modifiedFields[`auction_started`] ? 'bg-[#FFD69D]' : ''}`}
                        />
                     </label>
                     <label>
                        <span className="text-2xl">End: </span>
                        <input
                           type="datetime-local"
                           name="ended"
                           defaultValue={formatInTimeZone(new Date(auction.ended), timezone, 'yyyy-MM-dd HH:mm')}

                           onChange={(e) => handleAuctionFieldChange('ended', e.target.value)}  // Track changes
                           className={`p-2 border rounded-2xl ml-2 ${modifiedFields[`auction_ended`] ? 'bg-[#FFD69D]' : ''}`}
                        />
                     </label>
                     {
                        auction.status != 'ONGOING' ? (
                           <>
                              <button type="submit" className="bg-blue-500 text-white font-bold py-2 px-4 rounded hover:bg-blue-600 transition">
                                 UPDATE
                              </button>
                              <button onClick={() => handleStartAuction(auction.id)} className="bg-green-500 text-white font-bold py-2 px-4 rounded hover:bg-green-600 transition">
                                 START
                              </button>
                           </>
                        ) : (
                           <button onClick={() => handleStopAuction(auction.id)} className="bg-red-500 text-white font-bold py-2 px-4 rounded hover:bg-red-600 transition">
                              STOP
                           </button>
                        )
                     }
                  </div>
               </form>



               {/* Lot List */}
               <h2 className="text-lg font-bold mb-4">Lot List</h2>
               <div className="overflow-x-auto">
                  <div className="min-w-max">
                     {/* <!-- Header Row --> */}
                     <div className="flex bg-gray-100 font-bold text-center">
                        <div className="w-10 p-2">No.</div>
                        <div className="w-60 p-2">Start Time</div>
                        <div className="w-60 p-2">End Time</div>
                        <div className="w-40 p-2">Method</div>
                        <div className="w-40 p-2">Buyer Premium</div>
                        <div className="w-40 p-2">Seller Commission</div>
                        <div className="w-40 p-2">Starting Price</div>
                        <div className="w-40 p-2">Reserve Price</div>
                        <div className="w-40 p-2">Price Interval</div>
                        <div className="w-40 p-2">Estimated Price</div>
                        <div className="w-40 p-2">Buy Now Price</div>
                        <div className="w-40 p-2">Actions</div>
                     </div>

                     {lotList?.map((lot) => (
                        <>
                           <form key={lot.lotId} onSubmit={handleLotUpdate(lot.lotId)}>
                              <div key={lot.lotId} className="flex items-center border-b">
                                 <div className="w-10 p-2">{lot.lotId}</div>
                                 <div className="w-60 p-2">
                                    <input
                                       type="datetime-local"
                                       name="started"
                                       defaultValue={formatInTimeZone(new Date(lot.started), timezone, 'yyyy-MM-dd HH:mm')}


                                       onChange={(e) => handleLotFieldChange(lot.lotId, 'started', e.target.value)}  // Track changes
                                       className={`block w-full px-2 py-1 border rounded ${modifiedFields[`lot_${lot.lotId}_started`] ? 'bg-[#FFD69D]' : ''}`}
                                    />
                                 </div>
                                 <div className="w-60 p-2">
                                    <input
                                       type="datetime-local"
                                       name="ended"
                                       defaultValue={formatInTimeZone(new Date(lot.ended), timezone, 'yyyy-MM-dd HH:mm')}

                                       onChange={(e) => handleLotFieldChange(lot.lotId, 'ended', e.target.value)}  // Track changes
                                       className={`block w-full px-2 py-1 border rounded ${modifiedFields[`lot_${lot.lotId}_ended`] ? 'bg-[#FFD69D]' : ''}`}
                                    />
                                 </div>
                                 <div className="w-40 p-2">
                                    <select
                                       name="method"
                                       className="block w-full px-2 py-1 border rounded"
                                       defaultValue={lot.methodId}
                                    >
                                       <option value="1">Fixed Price</option>
                                       <option value="2">Sealed Bid</option>
                                       <option value="3">English</option>
                                       <option value="4">Dutch</option>
                                    </select>
                                 </div>
                                 <div className="w-40 p-2">
                                    <input
                                       type="number"
                                       min="0.00"
                                       step={0.01}
                                       name="buyerPremium"
                                       defaultValue={lot.buyerPremium}

                                       onChange={(e) => handleLotFieldChange(lot.lotId, 'buyerPremium', e.target.value)}  // Track changes
                                       className={`block w-full px-2 py-1 border rounded ${modifiedFields[`lot_${lot.lotId}_buyerPremium`] ? 'bg-[#FFD69D]' : ''}`}
                                    />
                                 </div>
                                 <div className="w-40 p-2">
                                    <input
                                       type="number"
                                       min="0.00"
                                       step={0.01}
                                       name="sellerCommission"
                                       defaultValue={lot.sellerCommission}

                                       onChange={(e) => handleLotFieldChange(lot.lotId, 'sellerCommision', e.target.value)}  // Track changes
                                       className={`block w-full px-2 py-1 border rounded ${modifiedFields[`lot_${lot.lotId}_sellerCommision`] ? 'bg-[#FFD69D]' : ''}`}
                                    />
                                 </div>
                                 <div className="w-40 p-2">
                                    <input
                                       type="number"
                                       name="startingPrice"
                                       defaultValue={lot.startingPrice}

                                       onChange={(e) => handleLotFieldChange(lot.lotId, 'startingPrice', e.target.value)}  // Track changes
                                       className={`block w-full px-2 py-1 border rounded ${modifiedFields[`lot_${lot.lotId}_startingPrice`] ? 'bg-[#FFD69D]' : ''}`}
                                    />
                                 </div>
                                 <div className="w-40 p-2">
                                    <input
                                       type="number"
                                       name="reservePrice"
                                       defaultValue={lot.reservePrice}

                                       onChange={(e) => handleLotFieldChange(lot.lotId, 'reservePrice', e.target.value)}  // Track changes
                                       className={`block w-full px-2 py-1 border rounded ${modifiedFields[`lot_${lot.lotId}_reservePrice`] ? 'bg-[#FFD69D]' : ''}`}
                                    />
                                 </div>
                                 <div className="w-40 p-2">
                                    <input
                                       type="number"
                                       name="priceInterval"
                                       defaultValue={lot.priceInterval}

                                       onChange={(e) => handleLotFieldChange(lot.lotId, 'priceInterval', e.target.value)}  // Track changes
                                       className={`block w-full px-2 py-1 border rounded ${modifiedFields[`lot_${lot.lotId}_priceInterval`] ? 'bg-[#FFD69D]' : ''}`}
                                    />
                                 </div>
                                 <div className="w-40 p-2">
                                    <input
                                       type="number"
                                       name="estimatedPrice"
                                       defaultValue={lot.estimatedPrice}

                                       onChange={(e) => handleLotFieldChange(lot.lotId, 'estimatedPrice', e.target.value)}  // Track changes
                                       className={`block w-full px-2 py-1 border rounded ${modifiedFields[`lot_${lot.lotId}_estimatedPrice`] ? 'bg-[#FFD69D]' : ''}`}
                                    />
                                 </div>
                                 <div className="w-40 p-2">
                                    <input
                                       type="number"
                                       name="buyNowPrice"
                                       defaultValue={lot.buyNowPrice}

                                       onChange={(e) => handleLotFieldChange(lot.lotId, 'buyNowPrice', e.target.value)}  // Track changes
                                       className={`block w-full px-2 py-1 border rounded ${modifiedFields[`lot_${lot.lotId}_buyNowPrice`] ? 'bg-[#FFD69D]' : ''}`}
                                    />
                                 </div>
                                 <div className="w-40 p-2 flex gap-2">
                                    {
                                       auction.status != 'ONGOING'
                                          && auction.status != 'UPCOMING' ? (
                                          <>
                                             <button
                                                type="submit"
                                                className="px-3 py-1 text-white bg-[#00B27D] rounded "
                                             >
                                                Update
                                             </button>
                                             <button
                                                type="button"
                                                className="px-3 py-1 text-white bg-red-500 rounded hover:bg-red-600"
                                             >
                                                Delete
                                             </button>
                                          </>
                                       ) : (
                                          <span>{lot.status}</span>
                                       )
                                    }

                                 </div>
                              </div>
                           </form>
                        </>
                     ))}
                  </div>
               </div>
            </>
         )}
      </div>
   )
}

export default NextEvent
