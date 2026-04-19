import React from 'react';
import StudentsImage from '../assets/students.jpeg';
import PageTransition from '../Components/PageTransition';
import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const HomePage = () => {
  const { user } = useAuth();

  return (
    <PageTransition>
      <div className="home-page">

        <div className="hero-section">
          <div className="hero-text">
            <h1>Find Scholarships <span className="hero-accent">Made for You</span></h1>
            <p>Scholarship Finder uses AI to match you with scholarships that fit your unique background, goals, and interests — so you can focus on what matters most.</p>
            <div className="hero-actions">
              {user ? (
                <Link to="/scholarships" className="hero-btn-primary">Browse Scholarships</Link>
              ) : (
                <>
                  <Link to="/register" className="hero-btn-primary">Get Started</Link>
                  <Link to="/login" className="hero-btn-secondary">Sign In</Link>
                </>
              )}
            </div>
          </div>
          <div className="hero-image-wrapper">
            <img src={StudentsImage} alt="Students" className="hero-image" />
          </div>
        </div>

        <div className="features-section">
          <div className="feature-card">
            <div className="feature-icon">🎯</div>
            <h3>AI-Powered Matching</h3>
            <p>Our algorithm analyzes your profile and finds scholarships aligned with your background, major, and career goals.</p>
          </div>
          <div className="feature-card">
            <div className="feature-icon">📋</div>
            <h3>Track Applications</h3>
            <p>Keep all your scholarship applications organized in one place and never miss a deadline.</p>
          </div>
          <div className="feature-card">
            <div className="feature-icon">🌐</div>
            <h3>Always Up to Date</h3>
            <p>Browse current scholarship opportunities organized in one place.</p>
          </div>
        </div>

        <div className="mission-section">
          <h2>Our Mission</h2>
          <p>Financial barriers should never stand in the way of academic success. Scholarship Finder is committed to making the search process easier, faster, and more personalized for every student.</p>
        </div>

      </div>
    </PageTransition>
  );
};

export default HomePage;