import { format } from "date-fns";

export const formatDate = (isoString) => {
  return format(new Date(isoString), "dd/MM/yyyy");
};

export const formatStatus = (statusList, statusProcesso) => {
  return statusList.find((status) => status.value === statusProcesso).label;
};

export const formatTextSize = (text, limit) => {
  if (!text) return "";
  return text.length > limit ? text.substring(0, limit) + "..." : text;
};
