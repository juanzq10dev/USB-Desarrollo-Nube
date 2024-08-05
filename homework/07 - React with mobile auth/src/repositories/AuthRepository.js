import { createUserWithEmailAndPassword, PhoneAuthCredential, signInWithEmailAndPassword, signInWithPhoneNumber } from "firebase/auth";
import { firebaseAuth } from "../firebase";

export const login = async (email, password) => {
    return new Promise((resolve, reject) => {
        signInWithEmailAndPassword(firebaseAuth, email, password)
            .then((userCredential) => {
                const user = userCredential.user;
                resolve(user);
            }).catch((error) => {
                const errorCode = error.code;
                const errorMessage = error.message;
                console.log(errorCode, errorMessage);

                reject(error);
            });
    });
}

export const phoneLogin = async (phoneNumber, appVerifier, verificationCode) => {
    return new Promise((resolve, reject) => {
        signInWithPhoneNumber(firebaseAuth, phoneNumber, appVerifier).then((confirmationResult) => {
            window.confirmationResult = confirmationResult
            confirmationResult.confirm(verificationCode).then((res) => {
                const user = res.user;
                resolve(user);
            })
        }).catch((error) => {
            const errorCode = error.code;
            const errorMessage = error.message;
            console.log(errorCode, errorMessage);

            reject(error);
        });
    })
}
export const register = async (email, password) => {
    return new Promise((resolve, reject) => {
        createUserWithEmailAndPassword(firebaseAuth, email, password)
            .then((userCredential) => {
                const user = userCredential.user;
                resolve(user);
            }).catch((error) => {
                const errorCode = error.code;
                const errorMessage = error.message;
                console.log(errorCode, errorMessage);

                reject(error);
            });
    });
}