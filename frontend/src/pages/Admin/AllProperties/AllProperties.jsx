import React from "react";
import "./AllProperties.css"
import { place_Dummy_list } from "../../../assets/assets";


const AllProperties = () => {
  return (
    <div className="list add flex-col">
      <p className="heading">All Properties List</p>

      <div className="list-table">
        {/* Header */}
        <div className="list-table-format title">
          <p>Image</p>
          <p>Title</p>
          <p>Type</p>
          <p>Price</p>
          <p>Action</p>
        </div>

        {/* Rows */}
        {place_Dummy_list.map((item) => (
          <div className="list-table-format" key={item.id}>
            <img src={item.images[0]} alt="" />
            <p>{item.name}</p>
            <p>{item.type}</p>
            <p>Rs {item.price}</p>
            <div className="actions">
              <button className="edit-btn">Edit</button>
              <button className="delete-btn">Delete</button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default AllProperties;