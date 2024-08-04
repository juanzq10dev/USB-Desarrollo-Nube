import { doc, setDoc } from "firebase/firestore"
import { db } from "../firebase"

export const updateProfile = async (email, name, lastName, age) => {
    return new Promise((resolve, reject) => {
        setDoc(doc(db, "users", email)) {
            name, 
            lastName,
            age
        }
    })
}