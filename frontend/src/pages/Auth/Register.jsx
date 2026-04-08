import React from "react";
import "./Auth.css";
import { assets } from "../../assets/assets";
import { Link } from "react-router-dom";


const Register = () => {
  
  return (
    <div className="auth-container">
      
      {/* LEFT SIDE */}
      <div className="auth-left">
        <h2>Create Account ✨</h2>
        <p className="subtitle">
          Join us today and start your journey.
        </p>

        <form className="auth-form">
          <label>Name</label>
          <input type="text" placeholder="Your name" />

          <label>Email</label>
          <input type="email" placeholder="Example@email.com" />

          <label>Password</label>
          <input type="password" placeholder="At least 8 characters" />

          <button className="primary-btn">Sign up</button>
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
        <img
          src={assets.rent_auth_cover}
          alt="auth visual"
        />
      </div>
    </div>
  );
};

export default Register;