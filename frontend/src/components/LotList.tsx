import { useEffect, useState } from 'react'
import LotItem from './LotItem'
import { getLotListByAuctionId } from '@/service/lotService'
import { useParams } from 'react-router-dom'
import { LotItemProps } from '@/type/lotItem'


const LotList = () => {
   const { auctionId } = useParams<{ auctionId: string }>();
   const [lotList, setLotList] = useState<LotItemProps[] | null>([])

   useEffect(() => {
      getLotListByAuctionId(auctionId).then((data) => {
         console.log(data)
         setLotList(data)
      }).catch((error) => {
         console.error(error)
      })
   }, [])

   return (
      <div className='grid grid-cols-3 gap-10 py-8 px-2'>
         {lotList?.map((lot) => (
            <LotItem {...lot} />
         ))}
      </div>
   )
}

export default LotList