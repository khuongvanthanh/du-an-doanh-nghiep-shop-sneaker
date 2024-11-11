import { createContext, useEffect, useState } from "react";
import { fetchCarts } from "~/apis/client/apiClient";

const CommonContext = createContext();

function CommonProvider({ children }) {
  const [amoutCart, setAmoutCart] = useState(null);
  const [carts, setCarts] = useState(null);

  const [filters, setFilters] = useState({
    pageNo: 0,
    pageSize: 10,
    keyword: "",
    categoryIds: null,
    brandIds: null,
    materialIds: null,
    minPrice: null,
    maxPrice: null,
    sortBy: "NONG HOANG VU - DEFAULT",
  });

  useEffect(() => {
    if (localStorage.getItem("accessToken")) {
      handleFetchCarts();
    }
  }, []);

  const handleFetchCarts = async () => {
    await fetchCarts()
      .then((response) => {
        setCarts(response.data);
        setAmoutCart(response.data?.length);
      })
      .catch((error) => {
        console.error("Fetch carts error:", error);
      });
  };

  const data = {
    amoutCart,
    setAmoutCart,
    carts,
    handleFetchCarts,
    filters,
    setFilters,
  };

  return (
    <CommonContext.Provider value={data}>{children}</CommonContext.Provider>
  );
}

export { CommonContext, CommonProvider };
