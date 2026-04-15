import React from 'react';

const ScholarshipCard = ({ scholarship, showApplyButton = false }) => {
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

  const handleApply = () => {
    // TODO: Navigate to application form or open modal
    alert(`Applying for ${name}`);
  };

  return (
    <div className="card scholarship-card">
      <h3>{name}</h3>
      {description && <p><strong>Description:</strong> {description}</p>}
      <p><strong>Amount:</strong> ${amount?.toLocaleString()}</p>
      <p><strong>Deadline:</strong> {formatDate(deadline)}</p>
      <p><strong>Field of Study:</strong> {fieldOfStudy}</p>
      {state && <p><strong>State:</strong> {state}</p>}
      {(eligibility || eligibilityCriteria) && (
        <p><strong>Eligibility:</strong> {eligibility || eligibilityCriteria}</p>
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
      {sourceApi && <p><strong>Source:</strong> {sourceApi}</p>}
      {showApplyButton && (
        applicationUrl ? (
          <a href={applicationUrl} target="_blank" rel="noopener noreferrer">
            <button className="apply-btn">Apply Now</button>
          </a>
        ) : (
          <button className="apply-btn" onClick={handleApply}>Apply Now</button>
        )
      )}
    </div>
  );
};

export default ScholarshipCard;
