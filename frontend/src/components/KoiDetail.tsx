import { getKoiById } from "@/service/koiService"
import { getLotsByKoiId } from "@/service/lotService"
import { Koi } from "@/type/koi"
import { Lot } from "@/type/lot"
import { formatMoney } from "@/util/helper"
import { formatInTimeZone } from "date-fns-tz"
import { useEffect, useState } from "react"
import { useParams } from "react-router-dom"


const KoiDetail = () => {
   const { koiId } = useParams()
   const [koi, setKoi] = useState<Koi | null>()
   const [lots, setLots] = useState<Lot[]>()
   const timezone = 'Asia/Bangkok'

   useEffect(() => {
      const fetchKoi = async () => {
         const koiData = await getKoiById(koiId)
         setKoi(koiData)
         if (koi?.status != 'PENDING' || koi?.status != 'REJECTED') {
            getLotsByKoiId(koiId).then((data) => {
               setLots(data.sort((a: Lot, b: Lot) => b.lotId - a.lotId))
            })
         }
      }
      fetchKoi()
   }, [])

   const lotDetail = (methodId: number, lot: Lot) => {
      switch (methodId) {
         case 1:
            return (<>
               <div className="text-left right-20 ">
                  <div className=" text-center text-xl font-semibold"> <div>{lot.methodName} Auction</div>
                  </div>
                  <div className="flex justify-between">
                     <div>Starting Price</div>
                     <div>₫{formatMoney(lot.startingPrice)}</div>
                  </div>


                  <div className="flex justify-between">
                     <div>Seller Commision</div>
                     <div>{lot.sellerCommission * 100} %</div>
                  </div>
               </div>
            </>)
         case 2:
            return (
               <div className="text-left right-20 ">
                  <div className=" text-center text-xl font-semibold"> <div>{lot.methodName} Auction</div>
                  </div>
                  <div className="flex justify-between">
                     <div>Starting Price</div>
                     <div>₫{formatMoney(lot.startingPrice)}</div>
                  </div>
                  <div className="flex justify-between">
                     <div>Estimated Price</div>
                     <div>₫{formatMoney(lot.estimatedPrice)}</div>
                  </div>


                  <div className="flex justify-between">
                     <div>Seller Commision</div>
                     <div>{lot.sellerCommission * 100} %</div>
                  </div>

               </div>
            )
         case 3:
            return (<>
               <div className="text-left right-20 ">
                  <div className=" text-center text-xl font-semibold"> <div>{lot.methodName} Auction</div>
                  </div>
                  <div className="flex justify-between">
                     <div>Starting Price</div>
                     <div>₫{formatMoney(lot.startingPrice)}</div>
                  </div>
                  <div className="flex justify-between">
                     <div>Price Interval</div>
                     <div>₫{formatMoney(lot.priceInterval)}</div>
                  </div>



                  <div className="flex justify-between">
                     <div>Seller Commision</div>
                     <div>{lot.sellerCommission * 100} %</div>
                  </div>

               </div>
            </>)
         case 4:
            return (<>
               <div className="text-left right-20 ">
                  <div className=" text-center text-xl font-semibold"> <div>{lot.methodName} Auction</div>
                  </div>
                  <div className="flex justify-between">
                     <div>Starting Price</div>
                     <div>₫{formatMoney(lot.startingPrice)}</div>
                  </div>
                  <div className="flex justify-between">
                     <div>Price Interval</div>
                     <div>₫{formatMoney(lot.priceInterval)}</div>
                  </div>
                  <div className="flex justify-between">
                     <div>Time Interval</div>
                     <div>{lot.timeInterval.toString().substring(2)}</div>
                  </div>


                  <div className="flex justify-between">
                     <div>Seller Commision</div>
                     <div>{lot.sellerCommission * 100} %</div>
                  </div>
               </div>
            </>)
      }
   }

   return (
      <>
         {
            !koi && <div>Loading...</div>
         }

         {
            koi && (<>
               <div className="grid grid-cols-5 gap-4">
                  <div className="grid col-span-2 gap-4 content-start">
                     <div className="bg-gradient-to-bl from-[#168dd2] to-[#1257aa] rounded-2xl">
                        <img className="object-contain aspect-[3/4] transition-opacity duration-500 p-6 drop-shadow-[8px_-8px_4px_rgba(0,0,0,0.25)] opacity-100" src={koi.image} alt="Koi" />
                     </div>
                  </div>

                  <div className="flex flex-col col-span-2 sm:col-span-3 overflow-y-auto">
                     <div className="card w-full mb-4 bg-[#F5F5F5] rounded-2xl">
                        <header className="card-header flex justify-between flex-wrap">
                           <div className="flex justify-between flex-wrap w-full px-4 pt-4">
                              <h1 className="text-2xl font-semibold mb-1">{koi.varietyName}</h1>
                           </div>

                        </header>
                        <section className="p-4 grid gap-2 grid-cols-2 text-lg">

                           <div className="bg-gray-300 rounded-full flex justify-between px-4 py-2">
                              <div className="">
                                 <span className="">
                                    <svg className="w-6 h-6 pb-1 inline text-blue-500 "
                                       xmlns="http://www.w3.org/2000/svg" aria-hidden="true" role="img" viewBox="0 0 24 24"><g fill="none" fill-rule="evenodd"><path d="m12.593 23.258l-.011.002l-.071.035l-.02.004l-.014-.004l-.071-.035q-.016-.005-.024.005l-.004.01l-.017.428l.005.02l.01.013l.104.074l.015.004l.012-.004l.104-.074l.012-.016l.004-.017l-.017-.427q-.004-.016-.017-.018m.265-.113l-.013.002l-.185.093l-.01.01l-.003.011l.018.43l.005.012l.008.007l.201.093q.019.005.029-.008l.004-.014l-.034-.614q-.005-.018-.02-.022m-.715.002a.02.02 0 0 0-.027.006l-.006.014l-.034.614q.001.018.017.024l.015-.002l.201-.093l.01-.008l.004-.011l.017-.43l-.003-.012l-.01-.01z"></path><path fill="currentColor" d="M7 9.5a7.5 7.5 0 1 1 2.942 5.957l-1.788 1.787L9.58 18.67a1 1 0 1 1-1.414 1.414L6.74 18.659l-2.12 2.12a1 1 0 0 1-1.414-1.415l2.12-2.12l-1.403-1.403a1 1 0 1 1 1.414-1.414L6.74 15.83l1.79-1.79A7.47 7.47 0 0 1 7 9.5M14.5 4a5.5 5.5 0 1 0 0 11a5.5 5.5 0 0 0 0-11"></path></g>
                                    </svg>
                                 </span>
                                 <span className="ml-3">Gender</span>
                              </div>
                              <div className="font-semibold">{
                                 koi.gender.charAt(0) + koi.gender.substring(1).toLocaleLowerCase()}
                              </div>
                           </div>

                           <div className="bg-gray-300 rounded-full flex justify-between px-4 py-2">
                              <div className="">
                                 <span className="">
                                    <svg
                                       className="w-6 h-6 pb-1 inline text-blue-500 "
                                       xmlns="http://www.w3.org/2000/svg" aria-hidden="true" role="img" viewBox="0 0 24 24"><path fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="m6 12l-1.586 1.586a2 2 0 0 0 0 2.828l3.172 3.172a2 2 0 0 0 2.828 0l9.172-9.172a2 2 0 0 0 0-2.828l-3.172-3.172a2 2 0 0 0-2.828 0L12 6m-6 6l2 2m-2-2l3-3m3-3l2 2m-2-2L9 9m0 0l3 3"></path>
                                    </svg>
                                 </span>
                                 <span className="ml-3">Length</span>
                              </div>
                              <div className="font-semibold">{
                                 koi.length} cm
                              </div>
                           </div>

                           <div className="bg-gray-300 rounded-full flex justify-between px-4 py-2">
                              <div className="">
                                 <span className="">
                                    <svg
                                       className="w-6 h-6 pb-1 inline text-blue-500 "
                                       xmlns="http://www.w3.org/2000/svg" aria-hidden="true" role="img" viewBox="0 0 24 24" ><g fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"><circle cx="12" cy="12" r="9"></circle><path d="M8 4v2a2 2 0 0 0 2 2h1a2 2 0 0 1 2 2v0a2 2 0 0 0 2 2v0a2 2 0 0 0 2-2v0a2 2 0 0 1 2-2h1m0 8h-3a2 2 0 0 0-2 2v2m-4 0v-2a2 2 0 0 0-2-2v0a2 2 0 0 1-2-2v0a2 2 0 0 0-2-2H3"></path></g>
                                    </svg>
                                 </span>
                                 <span className="ml-3">Breeder</span>
                              </div>
                              <div className="font-semibold">{
                                 koi.farmName}
                              </div>
                           </div>

                           <div className="bg-gray-300 rounded-full flex justify-between px-4 py-2">
                              <div className="">
                                 <span className="">
                                    <svg
                                       className="w-6 h-6 pb-1 inline text-blue-500 "
                                       xmlns="http://www.w3.org/2000/svg" aria-hidden="true" role="img" viewBox="0 0 24 24" ><g fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"><path d="M7 9a4 4 0 0 0-4 4v6a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-6a4 4 0 0 0-4-4v0M7 9h10M7 9V6m10 3V6m-5 0v3m0-6h.01M7 3h.01M17 3h.01"></path><path d="M3 13c0 1 .6 3 3 3s3-2 3-3c0 1 .6 3 3 3s3-2 3-3c0 1 .6 3 3 3s3-2 3-3"></path></g>
                                    </svg>
                                 </span>
                                 <span className="ml-3">Born in</span>
                              </div>
                              <div className="font-semibold">{
                                 koi.yob}
                              </div>
                           </div>

                        </section>
                        {/* Additional Info */}
                        <div className="flex flex-col gap-1 px-4">
                           <div>Status: <strong>{koi.status}</strong></div>
                           <div>Message: <strong>{koi.message}</strong></div>
                           <div>Sent: <strong>{formatInTimeZone(new Date(koi.sent), timezone, "dd.MM.yyyy HH:mm a")}</strong></div>
                           <div>Note: <strong>{koi.note}</strong></div>
                           <div>Replied: <strong>{koi.replied ? formatInTimeZone(new Date(koi.replied), timezone, "dd.MM.yyyy HH:mm a") : ''}</strong></div>
                        </div>

                        {/* Lot Overview */}
                        {
                           lots && lots.length > 0 && (<>
                              {
                                 lots.map((lot) => (
                                    <>
                                       <div className="p-4 border-2 border-gray-400 mt-2">
                                          <div>Lot #{lot.lotId}</div>
                                          {lotDetail(lot.methodId, lot)}
                                       </div>
                                    </>
                                 ))
                              }
                           </>)
                        }
                     </div>
                  </div>
               </div>
            </>)
         }
      </>)
}


export default KoiDetail