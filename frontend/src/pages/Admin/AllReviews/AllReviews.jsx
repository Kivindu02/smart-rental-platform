import React, { useEffect, useState } from "react";
import "./AllReviews.css";
import { getAllReviews, deleteReview } from "../../../services/reviewService";

const AllReviews = () => {
    const [reviews, setReviews] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        fetchReviews();
    }, []);

    const fetchReviews = async () => {
        try {
            const data = await getAllReviews();
            setReviews(data);
        } catch (err) {
            setError(err.response?.data?.message || "Failed to load reviews");
        } finally {
            setLoading(false);
        }
    };

    const handleDelete = async (id) => {
        if (!window.confirm("Are you sure you want to delete this review?")) return;
        try {
            await deleteReview(id);
            setReviews(reviews.filter(r => r.id !== id));
        } catch (err) {
            alert(err.response?.data?.message || "Failed to delete review");
        }
    };

    if (loading) return <p>Loading...</p>;
    if (error) return <p className="text-red-500">{error}</p>;

    return (
        <div className="list add flex-col">
            <p className="heading">All Reviews List</p>

            <div className="list-table">
                {/* Header */}
                <div className="list-table-format title">
                    <p>Customer</p>
                    <p>Comment</p>
                    <p>Property ID</p>
                    <p>Rating</p>
                    <p>Action</p>
                </div>

                {/* Rows */}
                {reviews.length === 0 && (
                    <p className="text-gray-500 p-4">No reviews found.</p>
                )}

                {reviews.map((review) => (
                    <div className="list-table-format" key={review.id}>

                        {/* Customer */}
                        <p>
                            {review.reviewer?.firstName} {review.reviewer?.lastName}
                        </p>

                        {/* Comment */}
                        <p>{review.comment}</p>

                        {/* Property ID */}
                        <p>{review.propertyId}</p>

                        {/* Rating */}
                        <p className="text-orange-400">
                            {"★".repeat(review.rating)}{"☆".repeat(5 - review.rating)}
                        </p>

                        {/* Actions */}
                        <div className="actions">
                            <button
                                className="delete-btn"
                                onClick={() => handleDelete(review.id)}
                            >
                                Delete
                            </button>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default AllReviews;