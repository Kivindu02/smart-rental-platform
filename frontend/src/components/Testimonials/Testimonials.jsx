import { useEffect, useState } from "react";

const testimonialsData = [
  { id: 1, date: "Jun 10, 2026", text: "PrebuiltUI has completely changed the way I write code. The components are clean, modern and production-ready.", name: "James Bond", role: "Amazon.com, Inc.", img: "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?q=80&w=200" },
  { id: 2, date: "Jun 10, 2026", text: "The components are beautifully designed and incredibly. PrebuiltUI fits perfectly into my React workflow.", name: "Emily Rodriguez", role: "The Walt Disney Company", img: "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?q=80&w=200" },
  { id: 3, date: "Jun 10, 2026", text: "PrebuiltUI is like having a professional design ready. It's become an essential part of my coding journey.", name: "Jack", role: "Facebook, Inc.", img: "https://images.unsplash.com/photo-1633332755192-727a05c4013d?q=80&w=200" },
  { id: 4, date: "Jul 12, 2026", text: "PrebuiltUI makes building polished interfaces effortless.", name: "Sarah Williams", role: "Spotify", img: "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?q=80&w=200" },
  { id: 5, date: "Jul 12, 2026", text: "PrebuiltUI delivers a perfect balance between design and development.", name: "Michael Chen", role: "Google LLC", img: "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?q=80&w=200" }
];

const Testimonials = () => {
  const [index, setIndex] = useState(0);
  const [isMobile, setIsMobile] = useState(window.innerWidth < 768);

  // Handle resize
  useEffect(() => {
    const handleResize = () => setIsMobile(window.innerWidth < 768);
    window.addEventListener("resize", handleResize);
    return () => window.removeEventListener("resize", handleResize);
  }, []);

  // Auto slide on mobile
  useEffect(() => {
    if (!isMobile) return;

    const timer = setInterval(() => {
      setIndex((prev) => (prev + 1) % testimonialsData.length);
    }, 3000);

    return () => clearInterval(timer);
  }, [isMobile]);

  // Visible items
  const count = isMobile ? 1 : 3;
  const visible = testimonialsData.slice(index, index + count);

  // Navigation
  const handleNext = () => {
    setIndex((prev) =>
      prev + count >= testimonialsData.length ? 0 : prev + count
    );
  };

  const handlePrev = () => {
    setIndex((prev) =>
      prev - count < 0
        ? Math.max(testimonialsData.length - count, 0)
        : prev - count
    );
  };

  return (
    <section className="py-0 px-4 sm:px-6 lg:px-8 bg-gray-50 pb-20">
      <div className="max-w-6xl mx-auto">

        {/* Heading */}
        <h1 className="text-4xl md:text-[40px] font-medium text-gray-900 text-center md:text-left">
          Loved by 10k+ People
        </h1>

        <p className="text-gray-600 mt-4 max-w-md text-center md:text-left mx-auto md:mx-0">
          Every single testimonial is a testament to the profound impact we strive to create every single day.
        </p>

        {/* Buttons */}
        {!isMobile && (
          <div className="flex justify-end gap-2 mt-4">
            <button onClick={handlePrev} className="h-10 w-10 rounded-lg bg-gray-100 border border-gray-300 flex items-center justify-center hover:bg-gray-200">
              <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="m12 19-7-7 7-7"/><path d="M19 12H5"/></svg>
            </button>
            <button onClick={handleNext} className="h-10 w-10 rounded-lg bg-gray-100 border border-gray-300 flex items-center justify-center hover:bg-gray-200">
              <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M5 12h14"/><path d="m12 5 7 7-7 7"/></svg>
            </button>
          </div>
        )}

        {/* Cards */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 mt-12">
          {visible.map((t) => (
            <div key={t.id} className="bg-white border border-gray-300 rounded-2xl p-6 shadow-sm hover:shadow-md transition">

              <div className="flex justify-between">
                <div className="text-orange-400">★★★★★</div>
                <p className="text-xs text-gray-500">{t.date}</p>
              </div>

              <p className="text-sm text-gray-600 mt-4">{t.text}</p>

              <div className="flex items-center gap-4 mt-6">
                <img src={t.img} className="w-12 h-12 rounded-full object-cover" />
                <div>
                  <p className="text-sm text-gray-800">{t.name}</p>
                  <p className="text-xs text-gray-500">{t.role}</p>
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