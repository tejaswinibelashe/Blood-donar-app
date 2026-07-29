import { initializeApp } from "https://www.gstatic.com/firebasejs/10.7.1/firebase-app.js";
import { getAuth, signInWithEmailAndPassword, createUserWithEmailAndPassword, onAuthStateChanged, signOut } from "https://www.gstatic.com/firebasejs/10.7.1/firebase-auth.js";
import { getDatabase, ref, push, onValue, set } from "https://www.gstatic.com/firebasejs/10.7.1/firebase-database.js";

// Firebase configuration for blood-donar-finder-7187a
const firebaseConfig = {
  apiKey: "AIzaSyDmb0dMu3ae_5INJkqMS1rJs0GKesQ88kc",
  authDomain: "blood-donar-finder-7187a.firebaseapp.com",
  databaseURL: "https://blood-donar-finder-7187a-default-rtdb.firebaseio.com",
  projectId: "blood-donar-finder-7187a",
  storageBucket: "blood-donar-finder-7187a.firebasestorage.app",
  messagingSenderId: "685067249547",
  appId: "1:685067249547:web:5ca20caf67fb84fb04b18e",
  measurementId: "G-7N94SLSGV4"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);
export const auth = getAuth(app);
export const db = getDatabase(app);

// Export Firebase methods
export { signInWithEmailAndPassword, createUserWithEmailAndPassword, onAuthStateChanged, signOut, ref, push, onValue, set };
