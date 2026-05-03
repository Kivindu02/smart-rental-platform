import React from "react";
import "./AllReviews.css"
import { place_Dummy_list } from "../../../assets/assets";


const AllReviews = () => {
  return (
    <div className="list add flex-col">
      <p className="heading">All Reviews List</p>

      <div className="list-table">
        {/* Header */}
        <div className="list-table-format title">
          <p>Customer</p>
          <p>Comment</p>
          <p>Property</p>
          <p>Rating</p>
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

export default AllReviews;