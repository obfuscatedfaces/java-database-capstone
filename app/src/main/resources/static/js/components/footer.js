
function renderFooter() {
    const footerDiv = document.getElementById("footer");
    if (!footerDiv) return;

    footerDiv.innerHTML = `
        <footer class="footer">
            <div class="footer-container">
                <div class="footer-logo-section">
                    <div class="footer-logo">
                        <img src="/assets/images/favicon.ico" alt="Ambulance" />
                        <p>©HealthCare Portal Project - Coursera.</p>
                    </div>
                </div>

                <div class="footer-columns">
                    <div class="footer-column">
                        <h4>Company</h4>
                        <a href="#">About Us</a>
                        <a href="#">Careers</a>
                        <a href="#">Press</a>
                    </div>
                    <div class="footer-column">
                        <h4>Support</h4>
                        <a href="#">Account</a>
                        <a href="#">Help Center</a>
                        <a href="#">Contact Us</a>
                    </div>
                    <div class="footer-column">
                        <h4>Legals</h4>
                        <a href="#">Terms of Service</a>
                        <a href="#">Privacy Policy</a>
                        <a href="#">Licensing</a>
                    </div>
                </div>
            </div>
        </footer>
    `;
}

renderFooter();