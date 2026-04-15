import React from 'react';

const ScholarshipCard = ({ scholarship }) => {
  if (!scholarship) return <div>No scholarship data</div>;

  const { name, description, amount, deadline, eligibilityCriteria, applicationUrl, fieldOfStudy, state, sourceApi } = scholarship;

  const formatDate = (dateString) => {
    if (!dateString) return 'N/A';
    return new Date(dateString).toLocaleDateString();
  };

  return (
    <div className="card scholarship-card">
      <h3>{name}</h3>
      <p><strong>Description:</strong> {description}</p>
      <p><strong>Amount:</strong> ${amount?.toLocaleString()}</p>
      <p><strong>Deadline:</strong> {formatDate(deadline)}</p>
      <p><strong>Field of Study:</strong> {fieldOfStudy}</p>
      <p><strong>State:</strong> {state}</p>
      <p><strong>Eligibility:</strong> {eligibilityCriteria}</p>
      <p><strong>Source:</strong> {sourceApi}</p>
      {applicationUrl && (
        <a href={applicationUrl} target="_blank" rel="noopener noreferrer">
          <button>Apply Now</button>
        </a>
      )}
    </div>
  );
};

export default ScholarshipCard;
