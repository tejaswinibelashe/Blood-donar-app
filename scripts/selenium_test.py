import unittest
from selenium import webdriver
from selenium.webdriver.chrome.options import Options

class SeleniumWebConnectivityTest(unittest.TestCase):
    def setUp(self):
        chrome_options = Options()
        chrome_options.add_argument("--headless")
        chrome_options.add_argument("--no-sandbox")
        chrome_options.add_argument("--disable-dev-shm-usage")
        self.driver = webdriver.Chrome(options=chrome_options)
    
    def test_github_repository_connectivity(self):
        print("\n[Selenium] Testing Web Connectivity...")
        self.driver.get("https://github.com/tejaswinibelashe/Blood-donar-app")
        title = self.driver.title
        self.assertIn("tejaswinibelashe", title)
        print("[Selenium] test_github_repository_connectivity PASSED")
        
    def test_firebase_console_availability(self):
        print("\n[Selenium] Testing Firebase Console Routing...")
        self.driver.get("https://firebase.google.com/")
        title = self.driver.title
        self.assertIn("Firebase", title)
        print("[Selenium] test_firebase_console_availability PASSED")

    def tearDown(self):
        self.driver.quit()

if __name__ == "__main__":
    unittest.main(verbosity=2)
