import React, { useState } from "react";
import "./Auth.css";
import { assets } from "../../assets/assets";
import { Link, useNavigate } from "react-router-dom";
import { register as registerService } from "../../services/authService";

const Register = () => {
    const navigate = useNavigate();
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);
    const [formData, setFormData] = useState({
        firstName: "",
        lastName: "",
        email: "",
        password: "",
        confirmPassword: ""
    });

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError("");

        // Client side validation
        if (formData.password !== formData.confirmPassword) {
            setError("Passwords do not match");
            setLoading(false);
            return;
        }

        try {
            await registerService(
                formData.firstName,
                formData.lastName,
                formData.email,
                formData.password,
                formData.confirmPassword
            );
            navigate("/login"); 
        } catch (err) {
            setError(err.response?.data?.message || "Registration failed");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="auth-container">

            {/* LEFT SIDE */}
            <div className="auth-left">
                <h2>Create Account ✨</h2>
                <p className="subtitle">
                    Join us today and start your journey.
                </p>

                {error && <p className="error-text">{error}</p>}

                <form className="auth-form" onSubmit={handleSubmit}>

                    <label>First Name</label>
                    <input
                        type="text"
                        name="firstName"
                        placeholder="Your first name"
                        value={formData.firstName}
                        onChange={handleChange}
                        required
                    />

                    <label>Last Name</label>
                    <input
                        type="text"
                        name="lastName"
                        placeholder="Your last name"
                        value={formData.lastName}
                        onChange={handleChange}
                        required
                    />

                    <label>Email</label>
                    <input
                        type="email"
                        name="email"
                        placeholder="Example@email.com"
                        value={formData.email}
                        onChange={handleChange}
                        required
                    />

                    <label>Password</label>
                    <input
                        type="password"
                        name="password"
                        placeholder="At least 8 characters"
                        value={formData.password}
                        onChange={handleChange}
                        required
                    />

                    <label>Confirm Password</label>
                    <input
                        type="password"
                        name="confirmPassword"
                        placeholder="Repeat your password"
                        value={formData.confirmPassword}
                        onChange={handleChange}
                        required
                    />

                    <button
                        className="primary-btn"
                        type="submit"
                        disabled={loading}
                    >
                        {loading ? "Signing up..." : "Sign up"}
                    </button>

                </form>

                <div className="divider">Or</div>

                <button className="social-btn google">Sign up with Google</button>
                <button className="social-btn facebook">Sign up with Facebook</button>

                <p className="bottom-text">
                    Already have an account? <Link to='/login'><span>Sign in</span></Link>
                </p>
            </div>

            {/* RIGHT SIDE */}
            <div className="auth-right">
                <img src={assets.rent_auth_cover} alt="auth visual" />
            </div>
        </div>
    );
};

export default Register;