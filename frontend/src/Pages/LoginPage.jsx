import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import axios from 'axios';
import { useAuth } from '../context/AuthContext';
import PageTransition from '../Components/PageTransition';

const LoginPage = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();
  const { login } = useAuth();
  const API = import.meta.env.VITE_API_URL ?? '';

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      const response = await axios.post(`${API}/api/users/login`, { username, password });
      if (response.data) login(response.data);
      navigate('/profile');
    } catch (err) {
      setError('Login failed. Please check your credentials.');
      console.error('Login error:', err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <PageTransition>
      <div className="page-container registration-page">
        <div className="auth-header">
          <h1>Welcome Back!</h1>
          <p>Sign in to continue discovering scholarships tailored to you.</p>
        </div>

        <div className="auth-content">
          <div className="card registration-card">
            <h3>Sign In</h3>
            <form onSubmit={handleSubmit}>
              <div className="form-group">
                <label htmlFor="username">Username:</label>
                <input
                  type="text"
                  id="username"
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
                  required
                />
              </div>

              <div className="form-group">
                <label htmlFor="password">Password:</label>
                <input
                  type="password"
                  id="password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  required
                />
              </div>

              {error && <div className="error-message">{error}</div>}

              <button type="submit" disabled={loading} className="register-btn">
                {loading ? 'Signing In...' : 'Login'}
              </button>
            </form>

            <div className="auth-links">
              <p>Don't have an account? <Link to="/register">Register here</Link></p>
            </div>
          </div>
        </div>

        <div className="auth-footer">
          <Link to="/" className="back-link">← Back to Home</Link>
        </div>
      </div>
    </PageTransition>
  );
};

export default LoginPage;
