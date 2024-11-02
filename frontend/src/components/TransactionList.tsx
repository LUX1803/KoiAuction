import { getTransaction } from "@/service/transactionService";
import { Transaction } from "@/type/transaction.d";
import { formatMoney } from "@/util/helper";
import { formatInTimeZone } from "date-fns-tz";
import { useEffect, useState } from "react";
import { NavLink } from "react-router-dom";

const TransactionList = () => {
   const [transactions, setTransactions] = useState<Transaction[]>([]);
   const timezone = 'Asia/Bangkok';

   useEffect(() => {
      const fetchTransactions = async () => {
         const data = await getTransaction();
         const paidTransactions = data.filter((transaction: Transaction) => transaction.status === 'SUCCESS');
         setTransactions(paidTransactions);
      };
      fetchTransactions();
   }, []);

   return (
      <div className="max-w-4xl mx-auto p-4">
         <h2 className="text-3xl font-bold text-center mb-8">Transaction History</h2>

         <div className="bg-white shadow-lg rounded-lg overflow-hidden border border-gray-200">
            <div className="grid grid-cols-4 bg-gray-200 text-gray-800 font-semibold p-4 text-sm">
               <div className="text-center">No.</div>
               <div className="text-center">Description</div>
               <div className="text-center">Amount</div>
               <div className="text-center">Time</div>
            </div>

            <div className="divide-y divide-gray-200">
               {transactions.map((transaction, index) => (
                  <div
                     key={transaction.id}
                     className="grid grid-cols-4 p-4 text-center text-gray-700 text-sm hover:bg-gray-50"
                  >
                     <div>{index + 1}</div>
                     <div className="truncate">{transaction.description}</div>
                     <div className="font-medium text-green-600">
                        {transaction.invoiceId ? ('-') : ('+')}
                        {formatMoney(transaction.amount)}</div>
                     <div>{formatInTimeZone(new Date(transaction.updated), timezone, "dd.MM.yyyy HH:mm a")}</div>
                  </div>
               ))}
               {transactions.length === 0 && (
                  <div className="p-4 text-center text-gray-500">
                     No transactions available.
                  </div>
               )}
            </div>
         </div>
      </div>
   );
};

export default TransactionList;
