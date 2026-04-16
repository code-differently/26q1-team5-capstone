import React, { useState } from 'react';
import ProfileCard from '../Components/ProfileCard';
// import MatchCard from '../Components/MatchCard';
import ApplicationCard from '../Components/ApplicationCard';

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
      <h1>Scholarship Tracker</h1>

      <ProfileCard
        profile={profile}
        isEditable
        onEdit={() => alert('Edit profile')}
      />

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

        <section className="applications-section">
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
        </section>
      </main>
    </div>
  );
};

export default HomePage;