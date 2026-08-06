import { useEffect, useState } from "react";
import { getReviewsByProperty, createReview } from "../../services/reviewService";
import { useAuth } from "../../context/AuthContext";

const Reviews = ({ propertyId }) => {
  const { isAuthenticated } = useAuth();
    const [reviews, setReviews] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");
    const [form, setForm] = useState({
        rating: 5,
        comment: ""
    });

    useEffect(() => {
        fetchReviews();
    }, [propertyId]);

    const fetchReviews = async () => {
        try {
            const data = await getReviewsByProperty(propertyId);
            setReviews(data);
        } catch (err) {
            setError(err.response?.data?.message || "Failed to load reviews");
        } finally {
            setLoading(false);
        }
    };

    const handleChange = (e) => {
        setForm({ ...form, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!isAuthenticated()) {
            alert("Please login to submit a review");
            return;
        }

        try {
            const newReview = await createReview(propertyId, form);
            setReviews([newReview, ...reviews]); // add to top
            setForm({ rating: 5, comment: "" }); // reset form
        } catch (err) {
            alert(err.response?.data?.message || "Failed to submit review");
        }
    };

    if (loading) return <p>Loading reviews...</p>;

  return (
    <div className="py-16 px-6 md:px-16 bg-gray-50 rounded-[20px]">

      {/* Title */}
      <h2 className="text-3xl font-semibold text-gray-900 mb-8">
        Customer Reviews
      </h2>

      {error && <p className="text-red-500 mb-4">{error}</p>}

      <div className="grid md:grid-cols-3 gap-8">

        {/* 🔹 Review List */}
        <div className="md:col-span-2 space-y-6">
          {reviews.length === 0 && (
              <p className="text-gray-500">No reviews yet. Be the first to review!</p>
          )}
          {reviews.map((r) => (
            <div key={r.id} className="bg-white p-5 rounded-xl shadow-sm border border-gray-300">

              {/* Header */}
              <div className="flex justify-between items-center">
                <p className="font-medium text-gray-800">{r.reviewer?.firstName} {r.reviewer?.lastName}</p>
                <p className="text-sm text-gray-500">{r.createdAt?.split('T')[0]}</p>
              </div>

              {/* Rating */}
              <div className="text-orange-400 mt-1">
                {"★".repeat(r.rating)}
                {"☆".repeat(5 - r.rating)}
              </div>

              {/* Comment */}
              <p className="text-gray-600 mt-2 text-sm">{r.comment}</p>
            </div>
          ))}
        </div>

        {/* 🔹 Review Form */}
        <div className="bg-white p-6 rounded-xl shadow-sm border border-gray-300 h-fit">

          <h3 className="text-lg font-semibold mb-4">Write a Review</h3>

          {!isAuthenticated() ? (
              <p className="text-gray-500 text-sm">Please login to write a review.</p>
          ) : (
              <form onSubmit={handleSubmit} className="space-y-4">
               
                {/* Rating */}
                <select
                  name="rating"
                  value={form.rating}
                  onChange={handleChange}
                  className="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm"
                >
                  {[5,4,3,2,1].map((r) => (
                    <option key={r} value={r}>{r} Stars</option>
                  ))}
                </select>

                {/* Comment */}
                <textarea
                  name="comment"
                  placeholder="Write your review..."
                  value={form.comment}
                  onChange={handleChange}
                  className="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm"
                  rows="4"
                  required
                />

                {/* Submit */}
                <button
                  type="submit"
                  className="w-full bg-orange-500 text-white py-2 rounded-lg hover:bg-orange-600 transition"
                >
                  Submit Review
                </button>

              </form>
          )}
        </div>

      </div>
    </div>
  );
};

export default Reviews;