import React from "react";
import "./Auth.css";
import { assets } from "../../assets/assets";
import { Link } from "react-router-dom";


const Login = () => {
  
  return (
    <div className="auth-container">
      
      {/* LEFT SIDE */}
      <div className="auth-left">
        <h2>Welcome Back 👋</h2>
        <p className="subtitle">
          Today is a new day. It's your day. You shape it. <br />
          Sign in to start managing your projects.
        </p>

        <form className="auth-form">
          <label>Email</label>
          <input type="email" placeholder="Example@email.com" />

          <label>Password</label>
          <input type="password" placeholder="At least 8 characters" />

          <p className="forgot">Forgot Password?</p>

          <button className="primary-btn">Sign in</button>
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