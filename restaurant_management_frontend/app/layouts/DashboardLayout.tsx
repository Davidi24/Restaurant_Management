import SideBar from "../components/general/sidebar/SideBar";
import React, { ReactNode } from 'react';

interface DashboardLayoutProps {
    children: ReactNode;
  }
  
const DashboardLayout: React.FC<DashboardLayoutProps> = ({ children }) => {
    return (
      <div className="dashboard-layout">
        <SideBar />
        <main>{children}</main>
      </div>
    );
  };

export default DashboardLayout