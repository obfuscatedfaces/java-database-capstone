
window.selectRole = function(role) {
    localStorage.setItem("userRole", role);
    const token = localStorage.getItem("token");


    if (role === "admin") {

        window.location.href = `/adminDashboard/${token}`;
    }
    else if (role === "doctor") {

        window.location.href = `/doctorDashboard/${token}`;
    }
    else if (role === "patient") {

        window.location.href = "/pages/patientDashboard.html";
    }
};


window.renderContent = function() {
    console.log("Rendering content for role: " + localStorage.getItem("userRole"));
};