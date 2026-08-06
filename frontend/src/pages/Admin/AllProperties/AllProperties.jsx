import React, { useEffect, useState } from "react";
import "./AllProperties.css"
import { getAllProperties, deleteProperty } from "../../../services/propertyService";
import { assets } from "../../../assets/assets";


const AllProperties = () => {
    const [properties, setProperties] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    // Fetch properties on mount
    useEffect(() => {
        fetchProperties();
    }, []);

    const fetchProperties = async () => {
        try {
            const data = await getAllProperties();
            setProperties(data);
        } catch (err) {
            setError(err.response?.data?.message || "Failed to load properties");
        } finally {
            setLoading(false);
        }
    };

    const handleDelete = async (id) => {
        if (!window.confirm("Are you sure you want to delete this property?")) return;
        try {
            await deleteProperty(id);
            // Remove from list without refetching
            setProperties(properties.filter(p => p.id !== id));
        } catch (err) {
            alert(err.response?.data?.message || "Failed to delete property");
        }
    };

    if (loading) return <p>Loading...</p>;
    if (error) return <p className="text-red-500">{error}</p>;
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
        {properties.map((item) => (
          <div className="list-table-format" key={item.id}>
            <img src={item.imageUrls?.[0] || assets.place_1} alt={item.name} onError={(e) => e.target.src = assets.place_1}/>
            <p>{item.name}</p>
            <p>{item.type}</p>
            <p>Rs {item.price}</p>
            <div className="actions">
              <button className="edit-btn">Edit</button>
              <button className="delete-btn" onClick={() => handleDelete(item.id)}>Delete</button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default AllProperties;