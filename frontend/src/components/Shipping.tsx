import { getKoiByStatus, updateKoi } from "@/service/koiService";
import { Koi } from "@/type/koi";
import { useEffect, useState } from "react";
import { Button } from "./ui";
import { getRecepient } from "@/service/memberService";


const Shipping = () => {
   const [koiList, setKoiList] = useState<Koi[]>([]);
   const [recipientList, setRecipientList] = useState<{ [koiId: number]: any }>({});

   useEffect(() => {
      getKoiByStatus('SHIPPING').then((data) => {
         setKoiList(data);

         data.forEach((koi: Koi) => {
            getRecepient(koi.id).then((recipientData) => {
               setRecipientList((prev) => ({ ...prev, [koi.id]: recipientData }));
            }).catch((error) => console.error(error));
         });
      });
   }, []);

   const handleUpdate = (koi: Koi): void => {
      updateKoi({ ...koi, status: "SOLD" }).then(() => {

         getKoiByStatus('SHIPPING').then((data) => {
            setKoiList(data);
         });
      });

   };

   return (
      <div>
         {koiList.length > 0 ? (
            <>
               <div className='grid grid-cols-3 gap-10 py-8 px-2 mt-8'>
                  {koiList.map((koi) => (
                     <div key={koi.id}>
                        <div className="block relative">
                           <span className="flex items-center font-semibold text-lg gap-2 ml-2 absolute text-black top-4 left-4">
                              <svg xmlns="http://www.w3.org/2000/svg" aria-hidden="true" role="img" width="1em" height="1em" viewBox="0 0 24 24" >
                                 <path fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 11.172V5a2 2 0 0 1 2-2h6.172a2 2 0 0 1 1.414.586l8 8a2 2 0 0 1 0 2.828l-6.172 6.172a2 2 0 0 1-2.828 0l-8-8A2 2 0 0 1 3 11.172M7 7h.001"></path>
                              </svg>
                              {koi.id}
                           </span>
                           <div className="flex justify-center bg-[#E8F0FE] rounded-t-2xl">
                              <img className="transform scale-90 object-contain aspect-[2/3]  rounded-lg  relative rounded-t-2xl h-5/6" loading="lazy" src={koi.image} alt='Koi' />
                           </div>
                           <div className="bg-[#76bace5d] rounded-b-2xl px-4 py-4 flex justify-between">

                              <span className="text-2xl font-semibold">{koi.varietyName}
                                 {recipientList[koi.id] && (
                                    <div className="text-sm mt-2">
                                       <p>Recipient: {recipientList[koi.id].firstname + ' ' + recipientList[koi.id].lastname}</p>
                                       <p>Address: {recipientList[koi.id].address}</p>
                                       <p>Phone: {recipientList[koi.id].phone}</p>
                                    </div>
                                 )}
                              </span>

                              <Button className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
                                 onClick={() => handleUpdate(koi)}>Delivered ✅
                              </Button>
                           </div>

                        </div>
                     </div>
                  ))}
               </div>
            </>
         ) : (
            <div className="text-center flex justify-center text-2xl font-bold mt-20">No koi orders to fulfill at this time.</div>
         )}
      </div>
   );
};

export default Shipping;