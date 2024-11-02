import React, { useEffect, useState } from "react";
import { formatMoney } from "@/util/helper";
import { useAuth } from "@/context/AuthContext";
import { getInvoiceById } from "@/service/invoiceService";
import { useParams } from "react-router-dom";
import { Invoice } from "@/type/invoice";
import { formatInTimeZone } from "date-fns-tz";

const BillingDetail = () => {
   const { user } = useAuth();
   const { invoiceId } = useParams();
   const [invoice, setInvoice] = useState<Invoice>(null);

   useEffect(() => {
      const fetchInvoice = async () => {
         const data = await getInvoiceById(invoiceId);
         setInvoice(data);
      };
      fetchInvoice();
   }, [invoiceId]);

   if (!invoice) return <div className="text-center mt-6 text-gray-600">Loading invoice details...</div>;

   return (
      <div className="max-w-2xl mx-auto p-8 bg-white shadow-md rounded-md text-gray-900 border border-gray-200">
         <h2 className="text-center text-3xl font-bold mb-4">Invoice #{invoice.id}</h2>
         <p className="text-center text-sm text-gray-500 mb-8">Date: {formatInTimeZone(new Date(invoice.created), "Asia/Bangkok", "dd.MM.yyyy HH:mm a")}</p>

         {/* Invoice Item Details */}
         <div className="mb-6">
            <div className="grid grid-cols-2 font-semibold bg-gray-100 text-lg text-gray-700 rounded-t-md">
               <div className="p-3 text-left border-b border-gray-300">Description</div>

               <div className="p-3 text-right border-b border-gray-300">Amount</div>
            </div>
            <div className="grid grid-cols-2 text-lg text-gray-700">
               <div className="p-4 border-b border-gray-200">{invoice.description}</div>

               <div className="p-4 text-right border-b border-gray-200">{formatMoney(invoice.hammerPrice)}</div>
            </div>
         </div>

         {/* Summary Section */}
         <div className="border-t pt-4 pr-5">
            <div className="flex justify-between mb-3">
               <span className="text-gray-600">Subtotal</span>
               <span>₫{formatMoney(invoice.hammerPrice)}</span>
            </div>
            <div className="flex justify-between mb-3">
               <span className="text-gray-600">Buyer Premium ({invoice.buyerPremium * 100}%)</span>
               <span>₫{formatMoney(invoice.hammerPrice * invoice.buyerPremium)}</span>
            </div>

            <div className="flex justify-between mb-3">
               <span className="text-gray-600">Shipping</span>
               <span>₫{formatMoney(invoice.shippingCost)}</span>
            </div>
            <div className="flex justify-between mb-3">
               <span className="text-gray-600">Tax / VAT ({invoice.tax * 100}%)</span>
               <span>₫{formatMoney((invoice.hammerPrice * (1 + invoice.buyerPremium) + invoice.shippingCost) * invoice.tax)}</span>
            </div>
            {/* Total */}
            <div className="flex justify-between font-bold text-xl pt-4 border-t border-gray-300 mt-4">
               <span>Total</span>
               <span>₫{formatMoney(invoice.buyerTotal)}</span>
            </div>
         </div>

         <p className="text-center mt-8 text-gray-500 italic text-lg">Thank you for your business!</p>
      </div>
   );
};

export default BillingDetail;
