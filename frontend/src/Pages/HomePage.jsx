import React, { useState } from 'react';
// import ProfileCard from '../Components/ProfileCard';
// import MatchCard from '../Components/MatchCard';
import ApplicationCard from '../Components/ApplicationCard';
import StudentsImage from '../assets/students.jpeg';

const HomePage = ({ matches, applications, profile, onDeleteApplication }) => {
  const handleEdit = (applicationId) => {
    console.log("Edit application:", applicationId);
    // TODO: Open edit modal or navigate to /applications/${applicationId}/edit
    alert(`Editing application ${applicationId}`);
  };

  const handleDelete = (applicationId) => {
    console.log("Delete application:", applicationId);
    if (onDeleteApplication) {
      onDeleteApplication(applicationId);
    }
  };

  return (
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

      {/* <ProfileCard
        profile={profile}
        isEditable
        onEdit={() => alert('Edit profile')}
      /> */}

      <main className="dashboard">
        {/* <section className="matches-section">
          <h2>Recommended For You</h2>
          <div className="card-grid">
            {matches.length > 0 ? (
              matches.map(match => (
                <MatchCard
                  key={match.scholarship.scholarshipId}
                  scholarship={match.scholarship}
                  matchScore={match.matchScore}
                />
              ))
            ) : (
              <p className="empty-state">No matches yet. Complete your profile to get recommendations.</p>
            )}
          </div>
        </section> */}

        {/* <section className="applications-section">
          <h2>Your Applications</h2>
          <div className="card-grid">
            {applications.length > 0 ? (
              applications.map(app => (
                <ApplicationCard
                  key={app.applicationId}
                  application={app}
                  onEdit={handleEdit}
                  onDelete={handleDelete}
                />
              ))
            ) : (
              <p className="empty-state">You haven't saved any applications yet.</p>
            )}
          </div>
        </section> */}
      </main>
    </div>
  );
};

export default HomePage;