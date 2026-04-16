import React, { useState, useEffect } from 'react';
import ScholarshipCard from '../Components/ScholarshipCard';

const MatchesPage = () => {
  const [matches, setMatches] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    fetchMatches();
  }, []);

  const fetchMatches = async () => {
    setLoading(true);
    try {
      // TODO: Replace with actual API call to /api/matches
      // For now, using mock data
      const mockMatches = [
        {
          scholarshipId: 1,
          name: "STEM Excellence Award",
          description: "Award for outstanding STEM students.",
          amount: 5000,
          deadline: "2026-05-01",
          eligibilityCriteria: "GPA 3.5+, STEM major",
          applicationUrl: "https://example.com/apply",
          fieldOfStudy: "Computer Science",
          state: "CA",
          sourceApi: "Internal"
        },
        {
          scholarshipId: 2,
          name: "Future Leaders Grant",
          description: "For aspiring leaders in business.",
          amount: 2500,
          deadline: "2026-04-15",
          eligibilityCriteria: "Business major, leadership experience",
          applicationUrl: "https://example.com/apply2",
          fieldOfStudy: "Business",
          state: "NY",
          sourceApi: "External"
        }
      ];
      setMatches(mockMatches);
    } catch (error) {
      console.error('Error fetching matches:', error);
    } finally {
      setLoading(false);
    }
  };

  const refreshMatches = async () => {
    // TODO: Call /api/matches/refresh
    alert('Refreshing matches...');
    fetchMatches();
  };

  return (
    <div>
      <h1>Your Matched Scholarships</h1>
      <button onClick={refreshMatches} disabled={loading}>
        {loading ? 'Refreshing...' : 'Refresh Matches'}
      </button>
      {loading ? (
        <p>Loading matches...</p>
      ) : matches.length > 0 ? (
        <div className="card-grid">
          {matches.map(match => (
            <ScholarshipCard key={match.scholarshipId} scholarship={match} />
          ))}
        </div>
      ) : (
        <p>No matches found. Update your profile to get better recommendations.</p>
      )}
    </div>
  );
};

export default MatchesPage;