export function capitalizeFirstLetter(text) {
  return text.charAt(0).toUpperCase() + text.slice(1);
}

export const convertTimeToMilliseconds = (timeString) => {
  const [hours, minutes, seconds] = timeString.split(":").map(Number);

  const hoursInMilliseconds = hours * 3600000; // 1 hour = 3600000 ms
  const minutesInMilliseconds = minutes * 60000; // 1 minute = 60000 ms
  const secondsInMilliseconds = seconds * 1000; // 1 second = 1000 ms

  return hoursInMilliseconds + minutesInMilliseconds + secondsInMilliseconds;
};

export function formatMoney(amount: number): string {
  // Convert the amount to a string
  const amountString: string = `${amount}`;

  // Format the integer part with a comma as a thousands separator
  const formattedIntegerPart = amountString.replace(/\B(?=(\d{3})+(?!\d))/g, ",");

  // Return the formatted amount
  return formattedIntegerPart;
}

