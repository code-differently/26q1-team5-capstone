import React, { useState, useEffect, useMemo } from 'react';
import axios from 'axios';
import ScholarshipCard from '../Components/ScholarshipCard';

const ScholarshipPage = () => {
  const [scholarships, setScholarships] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');

  useEffect(() => {
    const fetchScholarships = async () => {
      try {
        const response = await axios.get('/api/scholarships');
        const data = response.data.map(sch => ({
          scholarshipId: sch.scholarshipId,
          name: sch.name,
          description: sch.description,
          amount: sch.amount,
          deadline: sch.deadline,
          fieldOfStudy: sch.fieldOfStudy || 'General',
          eligibility: sch.eligibilityCriteria || 'Check application for details',
          requirements: [] // Backend doesn't have this, set to empty
        }));
        setScholarships(data);
      } catch (error) {
        console.error('Error fetching scholarships:', error);
        // Fallback to empty array or handle error
        setScholarships([]);
      } finally {
        setLoading(false);
      }
    };

    fetchScholarships();
  }, []);

  // Compute filtered scholarships in render instead of effect
  const filteredScholarships = useMemo(() => {
    let filtered = scholarships;

    if (searchTerm) {
      filtered = filtered.filter(scholarship =>
        scholarship.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
        scholarship.description.toLowerCase().includes(searchTerm.toLowerCase())
      );
    }

    return filtered;
  }, [searchTerm, scholarships]);

  if (loading) {
    return (
      <div className="page-container">
        <div className="loading">Loading scholarships...</div>
      </div>
    );
  }

  return (
    <div className="page-container">
      <h1>Browse Scholarships</h1>

      <div className="filters-section">
        <div className="search-bar">
          <input
            type="text"
            placeholder="Search scholarships..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
        </div>

      </div>

      <div className="scholarships-grid">
        {filteredScholarships.length > 0 ? (
          filteredScholarships.map(scholarship => (
            <ScholarshipCard
              key={scholarship.scholarshipId}
              scholarship={scholarship}
              showApplyButton={true}
            />
          ))
        ) : (
          <div className="empty-state">
            <p>No scholarships found matching your criteria.</p>
          </div>
        )}
      </div>
    </div>
  );
};

export default ScholarshipPage;