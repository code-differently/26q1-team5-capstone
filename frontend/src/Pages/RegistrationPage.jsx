import React from 'react';
import { Link } from 'react-router-dom';
import RegistrationCard from '../Components/RegistrationCard';
import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';

const RegistrationPage = () => {
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleRegistrationSuccess = (userData) => {
    console.log('User registered successfully:', userData);
    if (userData) {
      login(userData);
      navigate('/profile');
    }
  };

  return (
    <div className="page-container registration-page">
      <div className="auth-header">
        <h1>Join Scholarship Finder</h1>
        <p>Create your account to start discovering scholarships tailored to you.</p>
      </div>

      <div className="auth-content">
        <RegistrationCard
          onSuccess={handleRegistrationSuccess}
          redirectTo="/login"
        />
      </div>

      <div className="auth-footer">
        <p>By registering, you agree to our Terms of Service and Privacy Policy.</p>
        <Link to="/" className="back-link">← Back to Home</Link>
      </div>
    </div>
  );
};

export default RegistrationPage;