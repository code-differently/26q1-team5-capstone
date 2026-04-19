import React from 'react';
import StudentsImage from '../assets/students.jpeg';
import PageTransition from '../Components/PageTransition';

const HomePage = () => {
  return (
    <PageTransition>
      <div className="home-page">
        <h1>About Us</h1>
        <img
          src={StudentsImage}
          alt="Students"
          className="about-us-image"
        />
        <p>Scholarship Finder is dedicated to helping students discover and apply for scholarships that match their unique profiles.
          Our mission is to make the scholarship search process easier and more personalized,
          so students can focus on what matters most: their education and future careers.</p>
        <p>With our advanced matching algorithm, we analyze your profile and recommend scholarships that align with your background, interests, and goals.
          Whether you're a high school student looking for college scholarships or a college student seeking funding for graduate school, Scholarship Finder is here to support you every step of the way.</p>
        <p>Our team is passionate about education and believes that financial barriers should not stand in the way of academic success. We are committed to providing a user-friendly platform that empowers students to take control of their scholarship search and achieve their dreams.</p>
        <p>Join Scholarship Finder today and start discovering scholarships tailored to you!</p>
      </div>
    </PageTransition>
  );
};

export default HomePage;