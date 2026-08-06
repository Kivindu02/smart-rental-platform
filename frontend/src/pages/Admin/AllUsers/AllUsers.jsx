import React, { useEffect, useState } from "react";
import styles from "./AllUsers.module.css";
import { getAllUsers, deactivateUser, deleteUser } from "../../../services/authService";

const AllUsers = () => {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
      fetchUsers();
  }, []);

  const fetchUsers = async () => {
      try {
          const data = await getAllUsers();
          setUsers(data);
      } catch (err) {
          setError(err.response?.data?.message || "Failed to load users");
      } finally {
          setLoading(false);
      }
  };

  const handleDeactivate = async (id) => {
      if (!window.confirm("Deactivate this user?")) return;
      try {
          await deactivateUser(id);
          // Update status locally
          setUsers(users.map(u =>
              u.id === id ? { ...u, active: false } : u
          ));
      } catch (err) {
          alert(err.response?.data?.message || "Failed to deactivate user");
      }
  };

  const handleDelete = async (id) => {
      if (!window.confirm("Are you sure you want to delete this user?")) return;
      try {
          await deleteUser(id);
          setUsers(users.filter(u => u.id !== id));
      } catch (err) {
          alert(err.response?.data?.message || "Failed to delete user");
      }
  };

  if (loading) return <p>Loading...</p>;
  if (error) return <p>{error}</p>;

  return (
    <div className={styles.list}>
      <p className={styles.heading}>All Users List</p>

      <div className={styles.listTable}>

        {/* Header */}
        <div className={`${styles.listTableFormat} ${styles.title}`}>
            <p>Name</p>
            <p>Email</p>
            <p>Phone No</p>
            <p>Role</p>
            <p>Status</p>
            <p>Action</p>
        </div>

        {/* Rows */}
        {users.length === 0 && (
            <p className="p-4 text-gray-500">No users found.</p>
        )}

        {users.map((user) => (
          <div className={styles.listTableFormat} key={user.id}>

            {/* Name */}
            <p>{user.firstName} {user.lastName}</p>

            {/* Email */}
            <p>{user.email}</p>

            {/* Phone */}
            <p>{user.phoneNo || "N/A"}</p>

            {/* Role */}
            <p>{user.role}</p>

            {/* Status */}
            <p className={user.isActive ? "text-green-500" : "text-red-500"}>
                {user.active ? "Active" : "Inactive"}
            </p>

            {/* Actions */}
            <div className={styles.actions}>
                {user.active && (
                    <button
                      className={styles.activateBtn}
                      onClick={() => handleDeactivate(user.id)}
                      disabled={!user.active}
                  >
                      Deactivate
                  </button>
                )}
                <button
                    className={styles.deleteBtn}
                    onClick={() => handleDelete(user.id)}
                >
                    Delete
                </button>
              </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default AllUsers;