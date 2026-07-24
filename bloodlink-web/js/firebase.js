import { initializeApp } from "https://www.gstatic.com/firebasejs/10.7.1/firebase-app.js";
import { getAuth, signInWithEmailAndPassword, onAuthStateChanged, signOut } from "https://www.gstatic.com/firebasejs/10.7.1/firebase-auth.js";
import { getDatabase, ref, push, onValue, set } from "https://www.gstatic.com/firebasejs/10.7.1/firebase-database.js";

// Firebase configuration using extracted credentials from google-services.json
const firebaseConfig = {
  apiKey: "AIzaSyAgm8hVDmsyalJHsR4bjLyNX4-Tb_xb9WU",
  authDomain: "link-blood-bdaad.firebaseapp.com",
  databaseURL: "https://link-blood-bdaad-default-rtdb.firebaseio.com",
  projectId: "link-blood-bdaad",
  storageBucket: "link-blood-bdaad.appspot.com",
  messagingSenderId: "123456789", // Placeholder, not strictly required for Auth/DB
  appId: "1:123456789:web:abcdef" // Placeholder, not strictly required for Auth/DB in v10 for some services
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);
export const auth = getAuth(app);
export const db = getDatabase(app);

// Export Firebase methods
export { signInWithEmailAndPassword, onAuthStateChanged, signOut, ref, push, onValue, set };
