import React from 'react'
import { Accordion, AccordionContent, AccordionItem, AccordionTrigger } from '../ui/accordion'

function AboutPage() {
  return (
    <>
      <div>
        <div className='bg-gray-100 shadow-sm rounded-2xl px-8 py-6'>
          <h1 className='font-semibold text-2xl'>Welcome to KoiAuction</h1>
          <br />
          <p>
            With a deep passion for the artistry and beauty of Koi, we have created an online marketplace that brings together the finest breeders and the most discerning buyers. Our auctions provide a unique opportunity for Koi enthusiasts to acquire top-quality fish directly from renowned breeders in Japan, all from the comfort of their own homes.
          </p>
        </div>
        <div className='px-8 py-6 mt-4'>
          <h2 className='text-xl font-medium'>Frequently Asked Questions</h2>
          <Accordion type="single" collapsible>
            <AccordionItem value="item-1">
              <AccordionTrigger>How does shipping work and how much does it cost?</AccordionTrigger>
              <AccordionContent>
                When you win a koi, we'll reach out to you to schedule a shipping date. Shipping cost is 100,000 VND per koi within Vietnam.
              </AccordionContent>
            </AccordionItem>

            <AccordionItem value="item-2">
              <AccordionTrigger>When are payments due after an auction?</AccordionTrigger>
              <AccordionContent>
                Payments are due within 48 hours of the auction’s end and will only be accepted via VNPay and NCB Bank. We will attempt to charge your saved payment method automatically. If payment fails, we may contact you by phone or email to arrange payment. Failure to pay within the specified timeframe may result in cancellation of your order and re-listing of the koi on koiauction.com.
              </AccordionContent>
            </AccordionItem>

            <AccordionItem value="item-3">
              <AccordionTrigger>Is there bid sniping protection?</AccordionTrigger>
              <AccordionContent>
                Yes. bids made in final 5 minutes of an English auction lot will extend the auction by 5 minutes.
              </AccordionContent>
            </AccordionItem>
          </Accordion>
        </div>
        <div className='px-8 py-6 mt-4'>
          <h2 className='text-xl font-medium'>Contact</h2>
          <div className='mt-2'>
            <svg
              className='inline'
              xmlns="http://www.w3.org/2000/svg" aria-hidden="true" role="img" width="1.25rem" height="1.25rem" viewBox="0 0 24 24" >
              <path fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 18.675c1.93.83 4.242 1.325 7 1.325v-4l-4-1zm0 0C9.159 17.023 6.824 14.045 5.5 11m0 0C4.4 8.472 4 5.898 4 4h4l1 4z">

              </path>
            </svg>
            <span className='pl-2'>+ 84 332 000 999</span>
          </div>
          <div className='mt-2'>
            <svg
              className='inline'
              xmlns="http://www.w3.org/2000/svg" aria-hidden="true" role="img" width="1.25rem" height="1.25rem" viewBox="0 0 24 24" >
              <path fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="m7 9l3.75 3a2 2 0 0 0 2.5 0L17 9m4 8V7a2 2 0 0 0-2-2H5a2 2 0 0 0-2 2v10a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2">

              </path>
            </svg>
            <span className='pl-2'>contact@koiauction.com</span>
          </div>
        </div>
      </div>
    </>
  )
}

export default AboutPage