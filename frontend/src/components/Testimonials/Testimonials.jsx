import { useEffect, useState } from "react";
import { getAllReviews } from "../../services/reviewService";

const Testimonials = () => {
      const [reviews, setReviews] = useState([]);
    const [index, setIndex] = useState(0);
    const [isMobile, setIsMobile] = useState(window.innerWidth < 768);
    const [loading, setLoading] = useState(true);

    // Fetch real reviews
    useEffect(() => {
        const fetchReviews = async () => {
            try {
                const data = await getAllReviews();
                setReviews(data);
            } catch {
                // fallback to empty
            } finally {
                setLoading(false);
            }
        };
        fetchReviews();
    }, []);

    // Handle resize
    useEffect(() => {
        const handleResize = () => setIsMobile(window.innerWidth < 768);
        window.addEventListener("resize", handleResize);
        return () => window.removeEventListener("resize", handleResize);
    }, []);

    // Auto slide on mobile
    useEffect(() => {
        if (!isMobile || reviews.length === 0) return;
        const timer = setInterval(() => {
            setIndex((prev) => (prev + 1) % reviews.length);
        }, 3000);
        return () => clearInterval(timer);
    }, [isMobile, reviews]);

    const count = isMobile ? 1 : 3;
    const visible = reviews.slice(index, index + count);

    const handleNext = () => {
        setIndex((prev) =>
            prev + count >= reviews.length ? 0 : prev + count
        );
    };

    const handlePrev = () => {
        setIndex((prev) =>
            prev - count < 0
                ? Math.max(reviews.length - count, 0)
                : prev - count
        );
    };

    if (loading) return <p className="text-center py-10">Loading reviews...</p>;

    // Show message if no reviews yet
    if (reviews.length === 0) {
        return (
            <section className="py-0 px-4 sm:px-6 lg:px-8 bg-gray-50 pb-20">
                <div className="max-w-6xl mx-auto">
                    <h1 className="text-4xl md:text-[40px] font-medium text-gray-900 text-center md:text-left">
                        Loved by Our Community
                    </h1>
                    <p className="text-gray-500 mt-8 text-center">No reviews yet — be the first!</p>
                </div>
            </section>
        );
    }

  return (
    <section className="py-0 px-4 sm:px-6 lg:px-8 bg-gray-50 pb-20">
      <div className="max-w-6xl mx-auto">

        {/* Heading */}
        <h1 className="text-4xl md:text-[40px] font-medium text-gray-900 text-center md:text-left">
          Loved by Our Community
        </h1>

        <p className="text-gray-600 mt-4 max-w-md text-center md:text-left mx-auto md:mx-0">
          Every single review is a testament to the experience our renters have had.
        </p>

        {/* Buttons */}
        {!isMobile && reviews.length > 3 && (
          <div className="flex justify-end gap-2 mt-4">
            <button
              onClick={handlePrev}
              className="h-10 w-10 rounded-lg bg-gray-100 border border-gray-300 flex items-center justify-center hover:bg-gray-200"
          >
              <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                  <path d="m12 19-7-7 7-7"/><path d="M19 12H5"/>
              </svg>
          </button>
          <button
              onClick={handleNext}
              className="h-10 w-10 rounded-lg bg-gray-100 border border-gray-300 flex items-center justify-center hover:bg-gray-200"
          >
              <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                  <path d="M5 12h14"/><path d="m12 5 7 7-7 7"/>
              </svg>
          </button>
          </div>
        )}

        {/* Cards */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 mt-12">
          {visible.map((r) => (
            <div key={r.id} className="bg-white border border-gray-300 rounded-2xl p-6 shadow-sm hover:shadow-md transition">

              <div className="flex justify-between">
                <div className="text-orange-400">
                  {"★".repeat(r.rating)}{"☆".repeat(5 - r.rating)}
                  </div>
                <p className="text-xs text-gray-500">{r.createdAt?.split('T')[0]}</p>
              </div>

              <p className="text-sm text-gray-600 mt-4">{r.comment}</p>

              <div className="flex items-center gap-4 mt-6">
                <div className="w-12 h-12 rounded-full bg-orange-100 flex items-center justify-center text-orange-500 font-bold text-lg">
                    {r.reviewer?.firstName?.charAt(0)}
                </div>
                <div>
                  <p className="text-sm text-gray-800">
                    {r.reviewer?.firstName} {r.reviewer?.lastName}
                    </p>
                  <p className="text-xs text-gray-500">Verified Renter</p>
                </div>
              </div>

            </div>
          ))}
        </div>
      </div>
    </section>
  );
};

export default Testimonials;