"use client";

import { OrdersTable } from "./table";
// import styles from "./Counter.module.css";

export const Orders = () => {
  return (
    <div className="p-4">
      {/* <h2 className="text-xl font-bold mb-4">Orders</h2> */}
      <OrdersTable />
    </div>
  );
};
