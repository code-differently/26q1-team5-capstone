import React, { useState, useEffect } from 'react';
import ApplicationCard from '../Components/ApplicationCard';

const ApplicationPage = () => {
  const [applications, setApplications] = useState([]);
  const [loading, setLoading] = useState(true);
  const [filter, setFilter] = useState('ALL'); // ALL, SAVED, SUBMITTED, IN_PROGRESS

  // Mock data - replace with API call to ApplicationController
  const mockApplications = [
    {
      applicationId: 101,
      scholarship: {
        scholarshipId: 1,
        name: "STEM Excellence Award",
        amount: 5000,
        deadline: "2026-05-01"
      },
      status: "IN_PROGRESS",
      savedDate: "2026-03-10",
      submittedDate: null,
      deadlineAlert: "2026-05-01",
      notes: "Need recommendation letter from Prof. Smith"
    },
    {
      applicationId: 102,
      scholarship: {
        scholarshipId: 4,
        name: "Women in Tech Scholarship",
        amount: 4000,
        deadline: "2026-03-20"
      },
      status: "SUBMITTED",
      savedDate: "2026-02-20",
      submittedDate: "2026-03-01",
      deadlineAlert: "2026-03-15",
      notes: null
    },
    {
      applicationId: 103,
      scholarship: {
        scholarshipId: 3,
        name: "Community Service Award",
        amount: 3000,
        deadline: "2026-07-01"
      },
      status: "SAVED",
      savedDate: "2026-04-01",
      submittedDate: null,
      deadlineAlert: "2026-07-01",
      notes: "Gather volunteer hour logs"
    }
  ];

  useEffect(() => {
    // TODO: Replace with API call: axios.get('/api/applications')
    setTimeout(() => {
      setApplications(mockApplications);
      setLoading(false);
    }, 500);
  }, []); // eslint-disable-line react-hooks/exhaustive-deps

  const handleEdit = (applicationId) => {
    console.log("Edit application:", applicationId);
    // TODO: Navigate to edit page or open modal
    alert(`Editing application ${applicationId}`);
  };

  const handleDelete = (applicationId) => {
    console.log("Delete application:", applicationId);
    if (window.confirm('Are you sure you want to delete this application?')) {
      setApplications(applications.filter(app => app.applicationId !== applicationId));
      // TODO: API call: axios.delete(`/api/applications/${applicationId}`)
    }
  };

  const filteredApplications = applications.filter(app => {
    if (filter === 'ALL') return true;
    return app.status === filter;
  });

  const getStatusCounts = () => {
    const counts = { ALL: applications.length };
    applications.forEach(app => {
      counts[app.status] = (counts[app.status] || 0) + 1;
    });
    return counts;
  };

  const statusCounts = getStatusCounts();

  if (loading) {
    return (
      <div className="page-container">
        <div className="loading">Loading applications...</div>
      </div>
    );
  }

  return (
    <div className="page-container">
      <h1>My Applications</h1>

      <div className="filter-tabs">
        {Object.entries(statusCounts).map(([status, count]) => (
          <button
            key={status}
            className={`filter-tab ${filter === status ? 'active' : ''}`}
            onClick={() => setFilter(status)}
          >
            {status === 'ALL' ? 'All' : status.replace('_', ' ')} ({count})
          </button>
        ))}
      </div>

      <div className="applications-grid">
        {filteredApplications.length > 0 ? (
          filteredApplications.map(application => (
            <ApplicationCard
              key={application.applicationId}
              application={application}
              onEdit={handleEdit}
              onDelete={handleDelete}
            />
          ))
        ) : (
          <div className="empty-state">
            <p>No applications found in this category.</p>
            <p>Start applying for scholarships to track your progress!</p>
          </div>
        )}
      </div>
    </div>
  );
};

export default ApplicationPage;