import React from 'react';
import axios from 'axios';
import { useAuth } from '../context/AuthContext';
import './ScholarshipCard.css';

const API_BASE_URL = import.meta.env.VITE_API_URL;

const ScholarshipCard = ({ scholarship, showApplyButton = false }) => {
  const { user } = useAuth();

  if (!scholarship) return <div>No scholarship data</div>;

  const {
    name,
    description,
    amount,
    deadline,
    eligibility,
    eligibilityCriteria,
    requirements,
    applicationUrl,
    fieldOfStudy,
    state,
    sourceApi
  } = scholarship;

  const formatDate = (dateString) => {
    if (!dateString) return 'N/A';
    return new Date(dateString).toLocaleDateString();
  };

  const handleApply = async () => {
    if (!user?.userId) {
      alert('Please log in to apply for scholarships');
      return;
    }

    try {
      await axios.post(`${API_BASE_URL}/api/applications`, {
        userId: user.userId,
        scholarshipId: scholarship.scholarshipId
      });

      alert(
        `Application saved for ${name}! Check your applications page to track progress.`
      );

    } catch (error) {
      console.error('Error creating application:', error);

      const serverMessage =
        error.response?.data?.message ||
        error.response?.data ||
        error.message;

      alert(`Failed to save application. ${serverMessage}`);
    }
  };

  return (
    <div className="card scholarship-card">
      <h3>{name}</h3>

      {description && (
        <p><strong>Description:</strong> {description}</p>
      )}

      <p>
        <strong>Amount:</strong>{' '}
        {amount ? `$${amount.toLocaleString()}` : 'Amount not specified'}
      </p>

      <p>
        <strong>Deadline:</strong> {formatDate(deadline)}
      </p>

      <p>
        <strong>Field of Study:</strong> {fieldOfStudy}
      </p>

      {state && (
        <p><strong>State:</strong> {state}</p>
      )}

      {(eligibility || eligibilityCriteria) && (
        <p>
          <strong>Eligibility:</strong>{' '}
          {eligibility || eligibilityCriteria}
        </p>
      )}

      {requirements && requirements.length > 0 && (
        <div>
          <strong>Requirements:</strong>
          <ul>
            {requirements.map((req, index) => (
              <li key={index}>{req}</li>
            ))}
          </ul>
        </div>
      )}

      {sourceApi && (
        <p><strong>Source:</strong> {sourceApi}</p>
      )}

      {showApplyButton && (
        applicationUrl ? (
          <a
            href={applicationUrl}
            target="_blank"
            rel="noopener noreferrer"
          >
            <button className="apply-btn">Apply Now</button>
          </a>
        ) : (
          <button className="apply-btn" onClick={handleApply}>
            Apply Now
          </button>
        )
      )}
    </div>
  );
};

export default ScholarshipCard;