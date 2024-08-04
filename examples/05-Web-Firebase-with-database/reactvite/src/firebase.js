import { initializeApp } from "firebase/app";
import { getAuth } from "firebase/auth";
import { getFirestore } from "firebase/firestore";
// import { getAnalytics } from "firebase/analytics";

const firebaseConfig = {
    apiKey: "AIzaSyBQn8zk6_3qVI95y4vVhryPVu-mdC5JFH4",
    authDomain: "desarrollo-en-la-nube-27959.firebaseapp.com",
    projectId: "desarrollo-en-la-nube-27959",
    storageBucket: "desarrollo-en-la-nube-27959.appspot.com",
    messagingSenderId: "443127637273",
    appId: "1:443127637273:web:cc9be2f914ed4a03639188",
    measurementId: "G-GBKVW4QY5T"
  };

const firebaseApp = initializeApp(firebaseConfig);

// const analytics = getAnalytics(firebaseApp);

export const firebaseAuth = getAuth(firebaseApp);
export const db = getFirestore(firebaseApp)