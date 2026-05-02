import { useState } from "react";

const Reviews = () => {
  const [reviews, setReviews] = useState([
    {
      id: 1,
      name: "Kavindu",
      rating: 5,
      comment: "Amazing place! Clean and comfortable.",
      date: "2026-07-10",
    },
  ]);

  const [form, setForm] = useState({
    name: "",
    rating: 5,
    comment: "",
  });

  // Handle input change
  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  // Submit review
  const handleSubmit = (e) => {
    e.preventDefault();

    const newReview = {
      id: Date.now(),
      ...form,
      date: new Date().toISOString().split("T")[0],
    };

    setReviews([newReview, ...reviews]);

    setForm({ name: "", rating: 5, comment: "" });
  };

  return (
    <div className="py-16 px-6 md:px-16 bg-gray-50 rounded-[20px]">

      {/* Title */}
      <h2 className="text-3xl font-semibold text-gray-900 mb-8">
        Customer Reviews
      </h2>

      <div className="grid md:grid-cols-3 gap-8">

        {/* 🔹 Review List */}
        <div className="md:col-span-2 space-y-6">
          {reviews.map((r) => (
            <div key={r.id} className="bg-white p-5 rounded-xl shadow-sm border border-gray-300">

              {/* Header */}
              <div className="flex justify-between items-center">
                <p className="font-medium text-gray-800">{r.name}</p>
                <p className="text-sm text-gray-500">{r.date}</p>
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

          <form onSubmit={handleSubmit} className="space-y-4">

            {/* Name */}
            <input
              type="text"
              name="name"
              placeholder="Your name"
              value={form.name}
              onChange={handleChange}
              className="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm outline-none focus:ring-2 focus:ring-orange-400"
              required
            />

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
        </div>

      </div>
    </div>
  );
};

export default Reviews;