import React, { useState, useEffect, useMemo } from 'react';
import ScholarshipCard from '../Components/ScholarshipCard';

const ScholarshipPage = () => {
  const [scholarships, setScholarships] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedField, setSelectedField] = useState('');

  // Mock data - replace with API call to ScholarshipController
  const mockScholarships = [
    {
      scholarshipId: 1,
      name: "STEM Excellence Award",
      description: "Award for outstanding students in STEM fields",
      amount: 5000,
      deadline: "2026-05-01",
      fieldOfStudy: "Computer Science",
      eligibility: "3.5+ GPA, STEM major",
      requirements: ["Transcript", "Essay", "Recommendation"]
    },
    {
      scholarshipId: 2,
      name: "Future Leaders Grant",
      description: "For students showing leadership potential",
      amount: 2500,
      deadline: "2026-04-15",
      fieldOfStudy: "Business",
      eligibility: "Any major, leadership experience",
      requirements: ["Resume", "Essay"]
    },
    {
      scholarshipId: 3,
      name: "Arts & Humanities Fund",
      description: "Supporting creative and intellectual pursuits",
      amount: 3000,
      deadline: "2026-06-30",
      fieldOfStudy: "Liberal Arts",
      eligibility: "Arts/Humanities major",
      requirements: ["Portfolio", "Essay"]
    },
    {
      scholarshipId: 4,
      name: "Women in Tech Scholarship",
      description: "Empowering women in technology",
      amount: 4000,
      deadline: "2026-03-20",
      fieldOfStudy: "Computer Science",
      eligibility: "Female students, tech major",
      requirements: ["Transcript", "Essay", "Recommendation"]
    }
  ];

  useEffect(() => {
    // TODO: Replace with API call: axios.get('/api/scholarships')
    setTimeout(() => {
      setScholarships(mockScholarships);
      setLoading(false);
    }, 500);
  }, []); // eslint-disable-line react-hooks/exhaustive-deps

  // Compute filtered scholarships in render instead of effect
  const filteredScholarships = useMemo(() => {
    let filtered = scholarships;

    if (searchTerm) {
      filtered = filtered.filter(scholarship =>
        scholarship.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
        scholarship.description.toLowerCase().includes(searchTerm.toLowerCase())
      );
    }

    if (selectedField) {
      filtered = filtered.filter(scholarship => scholarship.fieldOfStudy === selectedField);
    }

    return filtered;
  }, [searchTerm, selectedField, scholarships]);

  const fieldsOfStudy = [...new Set(scholarships.map(s => s.fieldOfStudy))];

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

        <div className="field-filter">
          <select
            value={selectedField}
            onChange={(e) => setSelectedField(e.target.value)}
          >
            <option value="">All Fields of Study</option>
            {fieldsOfStudy.map(field => (
              <option key={field} value={field}>{field}</option>
            ))}
          </select>
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
            <button onClick={() => { setSearchTerm(''); setSelectedField(''); }}>
              Clear Filters
            </button>
          </div>
        )}
      </div>
    </div>
  );
};

export default ScholarshipPage;