"use client";

import * as React from "react";
import { add, format } from "date-fns";
import { Calendar as CalendarIcon } from "lucide-react";
import { cn } from "@/lib/utils";
import { Button } from "@/components/ui/button";
import { Calendar } from "@/components/ui/calendar";
import {
   Popover,
   PopoverContent,
   PopoverTrigger,
} from "@/components/ui/popover";
import { TimePickerDemo } from "./time-picker-demo";

interface DateTimePickerProps {
   value: Date | undefined;       // Accept the current value from the form
   onChange: (date: Date | undefined) => void; // Accept onChange function from the form
}

export function DateTimePicker({ value, onChange }: DateTimePickerProps) {
   const handleSelect = (newDay: Date | undefined) => {
      if (newDay) {
         const newDateFull = value ? add(newDay, { hours: value.getHours(), minutes: value.getMinutes() }) : newDay;
         onChange(newDateFull); // Update the form with the new date
      }
   };

   return (
      <Popover>
         <PopoverTrigger asChild>
            <Button
               variant={"outline"}
               className={cn(
                  "w-[280px] justify-start text-left font-normal",
                  !value && "text-muted-foreground"
               )}
            >
               <CalendarIcon className="mr-2 h-4 w-4" />
               {value ? format(value, "PPP HH:mm:ss") : <span>Pick a date</span>}
            </Button>
         </PopoverTrigger>
         <PopoverContent className="bg-white border border-gray-300 shadow-lg rounded-md w-auto p-0">
            <Calendar
               mode="single"
               selected={value}
               onSelect={handleSelect}
               initialFocus
            />
            <div className="p-3 border-t border-border">
               <TimePickerDemo setDate={onChange} date={value} />
            </div>
         </PopoverContent>
      </Popover>
   );
}
