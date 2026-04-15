import React from 'react';
import { Link } from 'react-router-dom';
import RegistrationCard from '../Components/RegistrationCard';

const RegistrationPage = () => {
  const handleRegistrationSuccess = (userData) => {
    console.log('User registered successfully:', userData);
    // Could show a success message or redirect with a message
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