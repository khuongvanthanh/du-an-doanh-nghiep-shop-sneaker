export const formatCurrencyVND = (price) => {
  return new Intl.NumberFormat("vi-VN", {
    style: "currency",
    currency: "VND",
    minimumFractionDigits: 0,
  }).format(price);
};

export const formatDateWithoutTime = (dateTimeString) => {
  const [datePart] = dateTimeString.split(" | ");
  return datePart;
};

export const formatDateTypeSecond = (isoDate) => {
  // input 0000-00-01T00:00
  const date = new Date(isoDate);
  const day = String(date.getDate()).padStart(2, "0");
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const year = date.getFullYear();

  return `${day}/${month}/${year}`;
};

export const formatDateTimeWithPending = (dateTimeString, message) => {
  if (!dateTimeString) return message;

  const [datePart, timePart] = dateTimeString.split(" | ");

  const [day, month, year] = datePart.split("/");

  const [hour, minute, second] = timePart.split(":");

  return `${hour}:${minute}:${second} ${day}/${month}/${year}`;
};
