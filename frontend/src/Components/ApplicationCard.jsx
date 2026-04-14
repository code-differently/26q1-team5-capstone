import React from 'react'

function ApplicationCard({ application, onEdit, onDelete }) {
  // onEdit and onDelete are the functions being called when "Edit" and "Delete" buttons are clicked.
  const { applicationId, scholarship, status, savedDate, submittedDate, deadlineAlert, notes } = application
  // Fields like scholarship name, status, saved date, submitted date, deadline alert, and notes are being extracted from the application object for display in the card.
  const formatDate = (dateString) => {
    if (!dateString) return 'N/A'
     return new Date(dateString).toLocaleDateString()
     //This formats the Date and if no date inputed it will return N/A
  }

  const getStatusColor = () => {
    switch (status) {
      case 'SAVED': return '#64748b'
      case 'IN_PROGRESS': return '#3b82f6'
      case 'SUBMITTED': return '#22c55e'
      case 'AWARDED': return '#eab308'
      case 'REJECTED': return '#ef4444'
      default: return '#64748b'

      //The application status color changes based off of the status of the application
    }
  }

  if (!scholarship) return <div className="card error">Scholarship data missing</div>

  return (
    <div className="card">
      <h3>{scholarship.name}</h3>
      <span className="status-badge" style={{ backgroundColor: getStatusColor() }}>
        {status}
      </span>
      <div className="card-details">
        <p><strong>Saved:</strong> {formatDate(savedDate)}</p>
        {submittedDate && <p><strong>Submitted:</strong> {formatDate(submittedDate)}</p>}
        <p><strong>Deadline Alert:</strong> {formatDate(deadlineAlert)}</p>
        {notes && <p><strong>Notes:</strong> {notes}</p>}
      </div>
      <div className="card-actions">
        <button className="btn-edit" onClick={() => onEdit(applicationId)}>Edit</button>
        <button className="btn-delete" onClick={() => onDelete(applicationId)}>Delete</button>
      </div>
    </div>
  )
}

export default ApplicationCard
