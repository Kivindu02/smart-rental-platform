import React from "react";
import styles from "./AllUsers.module.css";
import { place_Dummy_list } from "../../../assets/assets";


const AllUsers = () => {
  return (
<div className={styles.list}>
  <p className={styles.heading}>All Properties List</p>

  <div className={styles.listTable}>
    <div className={`${styles.listTableFormat} ${styles.title}`}>
      <p>Image</p>
      <p>Name</p>
      <p>Email</p>
      <p>Phone no</p>
      <p>Action</p>
    </div>

    {place_Dummy_list.map((item) => (
      <div className={styles.listTableFormat} key={item.id}>
        <img src={item.images[0]} alt="" />
        <p>{item.name}</p>
        <p>{item.type}</p>
        <p>Rs {item.price}</p>

        <div className={styles.actions}>
          <button className={styles.activateBtn}>Activate</button>
          <button className={styles.deleteBtn}>Delete</button>
        </div>
      </div>
    ))}
  </div>
</div>
  );
};

export default AllUsers;