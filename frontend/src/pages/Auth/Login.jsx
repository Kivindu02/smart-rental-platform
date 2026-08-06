import React, { useState } from "react";
import "./Auth.css";
import { assets } from "../../assets/assets";
import { Link, useNavigate } from "react-router-dom";
import { login as loginService } from "../../services/authService";
import { useAuth } from "../../context/AuthContext";


const Login = () => {
  const { login } = useAuth()
  const navigate = useNavigate();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError("");

    try {
            const data = await loginService(email, password);

            // Save token to localStorage
            login(data.token, data.role);

            // Redirect based on role
            if (data.role === "ADMIN") {
                navigate("/admin");
            } else {
                navigate("/");
            }
        } catch (err) {
            setError(err.response?.data?.message || "Invalid credentials");
        } finally {
            setLoading(false);
        }
  }
  
  return (
    <div className="auth-container">
      
      {/* LEFT SIDE */}
      <div className="auth-left">
        <h2>Welcome Back 👋</h2>
        <p className="subtitle">
          Today is a new day. It's your day. You shape it. <br />
          Sign in to start managing your projects.
        </p>

        {error && <p className="error-text">{error}</p>}

        <form className="auth-form" onSubmit={handleSubmit}>
          <label>Email</label>
          <input type="email" placeholder="Example@email.com" value={email} onChange={(e) => setEmail(e.target.value)} required/>

          <label>Password</label>
          <input type="password" placeholder="At least 8 characters" value={password} onChange={(e) => setPassword(e.target.value)} required/>

          <p className="forgot">Forgot Password?</p>

          <button className="primary-btn" disabled={loading}>
            {loading ? "Signing in..." : "Sign in"}
          </button>
        </form>

        <div className="divider">Or</div>

        <button className="social-btn google">Sign in with Google</button>
        <button className="social-btn facebook">Sign in with Facebook</button>

        <p className="bottom-text">
          Don't you have an account? <Link to='/register'><span>Sign up</span></Link>
        </p>

        <p className="footer">© 2026 ALL RIGHTS RESERVED</p>
      </div>

      {/* RIGHT SIDE */}
      <div className="auth-right">
        <img
          src={assets.rent_auth_cover}
          alt="auth visual"
        />
      </div>
    </div>
  );
};

export default Login;