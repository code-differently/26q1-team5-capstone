import React, { useState, useEffect } from 'react';
import ProfileCard from '../Components/ProfileCard';
import { Link, useNavigate } from 'react-router-dom';
import axios from 'axios';
import { useAuth } from '../context/AuthContext';

const ProfilePage = () => {
  const [profile, setProfile] = useState(null);
  const [loading, setLoading] = useState(true);
  const [isEditing, setIsEditing] = useState(false);
  const [editedProfile, setEditedProfile] = useState({});
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  
  useEffect(() => {
    if (!user) {
      setLoading(false);
      return;
    }

    const apiBase = import.meta.env.VITE_API_URL ?? '';
    const endpoint = apiBase ? `${apiBase}/api/profiles/${user.userId}` : `/api/profiles/${user.userId}`;

    const fetchProfile = async () => {
      try {
        setLoading(true);
        const res = await axios.get(endpoint);
        console.debug('Fetched profile response:', res && res.data);
        if (res && res.data) {
          setProfile(res.data);
          setEditedProfile(res.data);
        } else {
          setProfile(null);
          setEditedProfile({});
        }
      } catch (err) {
        // If 404 or no profile, treat as no profile
        console.warn('No profile found or fetch error', err);
        setProfile(null);
        setEditedProfile({});
      } finally {
        setLoading(false);
      }
    };

    fetchProfile();
  }, [user]); // eslint-disable-line react-hooks/exhaustive-deps

  const handleEdit = () => {
    setIsEditing(true);
  };

  const handleCreateClick = () => {
    // Prefill editedProfile with some sensible defaults (username -> name)
    const initial = {
      name: user?.username || '',
      gpa: null,
      major: '',
      enrollmentStatus: 'Full-time',
      needsFinancialAid: false,
      state: '',
      ethnicity: '',
      careerGoals: '',
      interests: ''
    };
    setEditedProfile(initial);
    setIsEditing(true);
  };

  const handleSave = () => {
    const apiBase = import.meta.env.VITE_API_URL ?? '';

    const save = async () => {
      try {
        setLoading(true);
        if (!profile) {
          // create profile: backend expects Profile with nested user.userId
          const endpoint = apiBase ? `${apiBase}/api/profiles` : `/api/profiles`;
          const payload = {
            user: { userId: user.userId },
            name: editedProfile.name,
            gpa: editedProfile.gpa,
            major: editedProfile.major,
            enrollmentStatus: editedProfile.enrollmentStatus,
            needsFinancialAid: editedProfile.needsFinancialAid,
            state: editedProfile.state,
            ethnicity: editedProfile.ethnicity,
            careerGoals: editedProfile.careerGoals,
            interests: editedProfile.interests
          };
          const res = await axios.post(endpoint, payload);
          console.debug('Create profile response:', res && res.data);
          if (res && res.data) {
            setProfile(res.data);
            setEditedProfile(res.data);
          } else {
            // fallback: refetch created profile for user
            const fetchRes = await axios.get(apiBase ? `${apiBase}/api/profiles/${user.userId}` : `/api/profiles/${user.userId}`);
            setProfile(fetchRes.data);
            setEditedProfile(fetchRes.data);
          }
          setIsEditing(false);
          alert('Profile created successfully!');
        } else {
          // update profile
          const endpoint = apiBase ? `${apiBase}/api/profiles/${profile.profileId}` : `/api/profiles/${profile.profileId}`;
          const payload = {
            name: editedProfile.name,
            gpa: editedProfile.gpa,
            major: editedProfile.major,
            enrollmentStatus: editedProfile.enrollmentStatus,
            needsFinancialAid: editedProfile.needsFinancialAid,
            state: editedProfile.state,
            ethnicity: editedProfile.ethnicity,
            careerGoals: editedProfile.careerGoals,
            interests: editedProfile.interests
          };
          const res = await axios.put(endpoint, payload);
          console.debug('Update profile response:', res && res.data);
          if (res && res.data) {
            setProfile(res.data);
            setEditedProfile(res.data);
          } else {
            // fallback: refetch profile to ensure UI shows latest data
            const fetchRes = await axios.get(apiBase ? `${apiBase}/api/profiles/${user.userId}` : `/api/profiles/${user.userId}`);
            setProfile(fetchRes.data);
            setEditedProfile(fetchRes.data);
          }
          setIsEditing(false);
          alert('Profile updated successfully!');
        }
      } catch (err) {
        console.error('Save profile error', err);
        alert('Failed to save profile.');
      } finally {
        setLoading(false);
      }
    };

    save();
  };

  const handleCancel = () => {
    setEditedProfile(profile || {});
    setIsEditing(false);
  };

  const handleDelete = () => {
    const apiBase = import.meta.env.VITE_API_URL ?? '';
    const endpoint = apiBase ? `${apiBase}/api/profiles/${user.userId}` : `/api/profiles/${user.userId}`;

    const deleteProfile = async () => {
      try {
        setLoading(true);
        const response = await axios.delete(endpoint);
        console.debug('Profile deleted successfully', response);
        setProfile(null);
        setEditedProfile({});
        alert('Profile and account deleted successfully. Logging you out.');
        // Logout the user
        logout();
        // Navigate to home page
        navigate('/');
      } catch (err) {
        console.error('Delete profile error:', err);
        console.error('Error response:', err.response);
        console.error('Error message:', err.message);
        const errorMessage = err.response?.data?.message || err.message || 'Failed to delete profile. Please try again.';
        alert(errorMessage);
      } finally {
        setLoading(false);
      }
    };

    deleteProfile();
  };

  const handleProfileChange = (field, value) => {
    setEditedProfile(prev => ({
      ...prev,
      [field]: value
    }));
  };

  if (loading) {
    return (
      <div className="page-container">
        <div className="loading">Loading profile...</div>
      </div>
    );
  }

  return (
    <div className="page-container">
      <h1>My Profile</h1>

      {isEditing ? (
        <div className="profile-edit-form">
          <h2>{profile ? 'Edit Profile' : 'Create Profile'}</h2>
          <div className="form-grid">
            <div className="form-group">
              <label>Name:</label>
              <input
                type="text"
                value={editedProfile.name || ''}
                onChange={(e) => handleProfileChange('name', e.target.value)}
              />
            </div>

            <div className="form-group">
              <label>GPA:</label>
              <input
                type="number"
                step="0.1"
                min="0"
                max="4.0"
                value={editedProfile.gpa || ''}
                onChange={(e) => {
                  const val = e.target.value;
                  handleProfileChange('gpa', val === '' ? null : parseFloat(val));
                }}
              />
            </div>

            <div className="form-group">
              <label>Ethnicity:</label>
              <input
                type="text"
                value={editedProfile.ethnicity || ''}
                onChange={(e) => handleProfileChange('ethnicity', e.target.value)}
              />
            </div>

            <div className="form-group">
              <label>Major:</label>
              <input
                type="text"
                value={editedProfile.major || ''}
                onChange={(e) => handleProfileChange('major', e.target.value)}
              />
            </div>

            <div className="form-group">
              <label>Enrollment Status:</label>
              <select
                value={editedProfile.enrollmentStatus || ''}
                onChange={(e) => handleProfileChange('enrollmentStatus', e.target.value)}
              >
                <option value="Full-time">Full-time</option>
                <option value="Part-time">Part-time</option>
                <option value="Graduate">Graduate</option>
              </select>
            </div>


            <div className="form-group">
              <label>State:</label>
              <input
                type="text"
                value={editedProfile.state || ''}
                onChange={(e) => handleProfileChange('state', e.target.value)}
              />
            </div>

            <div className="form-group">
              <label>Career Goals:</label>
              <textarea
                value={editedProfile.careerGoals || ''}
                onChange={(e) => handleProfileChange('careerGoals', e.target.value)}
                rows="3"
              />
            </div>

            <div className="form-group">
              <label>Interests:</label>
              <textarea
                value={editedProfile.interests || ''}
                onChange={(e) => handleProfileChange('interests', e.target.value)}
                rows="2"
              />
            </div>

            {/* Fields matching backend Profile entity */}
          </div>

          <div className="form-actions">
            <button onClick={handleSave} className="save-btn">Save Changes</button>
            <button onClick={handleCancel} className="cancel-btn">Cancel</button>
          </div>
        </div>
      ) : (
        user ? (
          profile ? (
            <ProfileCard
              profile={profile}
              isEditable={true}
              onEdit={handleEdit}
              onDelete={handleDelete}
            />
          ) : (
            <div className="no-profile">
              <p>No profile data yet. Create your profile to get tailored scholarship matches.</p>
              <button onClick={handleCreateClick} className="create-btn">Create Profile</button>
            </div>
          )
        ) : (
          <div className="no-profile">
            <p>Please <Link to="/login">login</Link> to view or create your profile.</p>
          </div>
        )
      )}
    </div>
  );
};

export default ProfilePage;