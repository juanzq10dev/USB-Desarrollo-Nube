import { arrayUnion, doc, getDoc, setDoc, updateDoc } from "firebase/firestore"
import { firebaseDb } from "../firebase"
import firebase from "firebase/compat/app";

const usersCollection = "users";
export const updateProfile = async (email, name, lastName, age) => {
    return new Promise((resolve, reject) => {
        setDoc(doc(firebaseDb, usersCollection, email), {
            name,
            lastName,
            age,
            email
        }).then(() => {
            resolve();
        }).catch((error) => {
            console.log(error);
            reject(error);
        });
    });
}
export const getProfile = async (email) => {
    return new Promise((resolve, reject) => {
        getDoc(doc(firebaseDb, usersCollection, email)).then((doc) => {
            if (doc.exists()) {
                resolve(doc.data());
            } else {
                resolve(null);
            }
        }).catch((error) => {
            console.log(error);
            reject(error);
        });
    });
}

export const addPhoneNumber = async (email, contactName, phoneNumber) => {
    const ref = doc(firebaseDb, usersCollection, email)

    await updateDoc(ref, {
        phoneNumbers: arrayUnion({name: contactName, phoneNumber: phoneNumber})
    }).catch((err) => {
        console.error("Error al actualizar el arreglo:", err);
        return false
    }) 
    return true 
}