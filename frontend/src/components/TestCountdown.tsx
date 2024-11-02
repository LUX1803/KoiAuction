import React from 'react'
import Countdown from 'react-countdown';

const TestCountdown = () => {
   const renderer = ({ days, hours, minutes, seconds, completed }) => {
      if (completed) {
         return <span>Auction Ended!</span>;
      } else {
         return (
            <div>
               <span>{days}d {hours}h {minutes}m {seconds}s</span>
            </div>
         );
      }
   };
   const t = "2024-10-16T13:5:00.000+00:00";
   const formate = (date) => {
      return date.replace(/:(\d)(?=\d{2}\.\d+)/, ':0$1');
   }
   const t1 = new Date(formate(t));
   console.log(t1)
   return (
      <Countdown date={t} renderer={renderer} />
   )
}

export default TestCountdown