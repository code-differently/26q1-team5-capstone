import React from 'react'
import './ProfileCard.css'

// Maps to Profile entity in UML
function ProfileCard({ profile, isEditable, onEdit, onDelete }) {
  if (!profile) return <div>No profile data</div>

  const { name, gpa, major, enrollmentStatus, needsFinancialAid, state, ethnicity, careerGoals, interests } = profile || {};

  const handleDelete = () => {
    if (window.confirm('Are you sure you want to delete your profile? This action cannot be undone.')) {
      onDelete();
    }
  };

  return (
    <div className="card profile-card">
      <div className="card-header">
        <h3>{name}</h3>
        <div className="card-header-buttons">
          {isEditable && <button onClick={onEdit} className="edit-btn">Edit</button>}
          {isEditable && <button onClick={handleDelete} className="delete-btn">Delete Profile</button>}
        </div>
      </div>
      <div className="card-body">
        <p><strong>GPA:</strong> {gpa ?? '—'}</p>
        <p><strong>Major:</strong> {major || '—'}</p>
        <p><strong>Status:</strong> {enrollmentStatus || '—'}</p>
        <p><strong>State:</strong> {state || '—'}</p>
        <p><strong>Ethnicity:</strong> {ethnicity || '—'}</p>
        <p><strong>Financial Aid Needed:</strong> {needsFinancialAid == null ? '—' : (needsFinancialAid ? 'Yes' : 'No')}</p>
        <p><strong>Career Goals:</strong> {careerGoals || '—'}</p>
        <p><strong>Interests:</strong> {interests || '—'}</p>
      </div>
    </div>
  )
}

export default ProfileCard